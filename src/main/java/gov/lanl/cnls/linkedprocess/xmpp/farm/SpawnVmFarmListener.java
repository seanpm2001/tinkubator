package gov.lanl.cnls.linkedprocess.xmpp.farm;

import gov.lanl.cnls.linkedprocess.os.errors.UnsupportedScriptEngineException;
import gov.lanl.cnls.linkedprocess.os.errors.VMAlreadyExistsException;
import gov.lanl.cnls.linkedprocess.os.errors.VMSchedulerIsFullException;
import gov.lanl.cnls.linkedprocess.LinkedProcess;
import gov.lanl.cnls.linkedprocess.xmpp.vm.XmppVirtualMachine;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;

/**
 * User: marko
 * Date: Jun 25, 2009
 * Time: 11:23:49 AM
 */
public class SpawnVmFarmListener implements PacketListener {

    private XmppFarm farm;

    public SpawnVmFarmListener(XmppFarm farm) {
        this.farm = farm;
    }

    public void processPacket(Packet spawnVm) {
        try {
            processPacketTemp(spawnVm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processPacketTemp(Packet spawnVm) {
        XmppFarm.LOGGER.info("Arrived " + SpawnVmFarmListener.class.getName());
        XmppFarm.LOGGER.info(spawnVm.toXML());

        SpawnVm returnSpawnVm = new SpawnVm();
        returnSpawnVm.setTo(spawnVm.getFrom());
        returnSpawnVm.setPacketID(spawnVm.getPacketID());
        String vmSpecies = ((SpawnVm) spawnVm).getVmSpecies();
        if(vmSpecies == null) {
            returnSpawnVm.setErrorType(LinkedProcess.ErrorType.MALFORMED_PACKET);
            returnSpawnVm.setErrorMessage("spawn_vm XML packet is missing the vm_species attribute");
            returnSpawnVm.setType(IQ.Type.ERROR);
        } else {
            try {
                XmppVirtualMachine vm = farm.spawnVirtualMachine(spawnVm.getFrom(), vmSpecies);
                returnSpawnVm.setVmJid(vm.getFullJid());
                returnSpawnVm.setVmPassword(vm.getVmPassword());
                returnSpawnVm.setVmSpecies(vmSpecies);
                returnSpawnVm.setType(IQ.Type.RESULT);
            } catch (VMAlreadyExistsException e) {
                returnSpawnVm.setErrorType(LinkedProcess.ErrorType.INTERNAL_ERROR);
                returnSpawnVm.setErrorMessage(e.getMessage());
                returnSpawnVm.setType(IQ.Type.ERROR);
            } catch (VMSchedulerIsFullException e) {
                returnSpawnVm.setErrorType(LinkedProcess.ErrorType.FARM_IS_BUSY);
                returnSpawnVm.setErrorMessage(e.getMessage());
                returnSpawnVm.setType(IQ.Type.ERROR);
            } catch (UnsupportedScriptEngineException e) {
                returnSpawnVm.setErrorType(LinkedProcess.ErrorType.SPECIES_NOT_SUPPORTED);
                returnSpawnVm.setErrorMessage(e.getMessage());
                returnSpawnVm.setType(IQ.Type.ERROR);
            }
        }

        XmppFarm.LOGGER.info("Sent " + SpawnVmFarmListener.class.getName());
        XmppFarm.LOGGER.info(returnSpawnVm.toXML());
        farm.getConnection().sendPacket(returnSpawnVm);
    }
}

