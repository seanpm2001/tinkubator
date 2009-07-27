package org.linkedprocess.gui.villein;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.linkedprocess.xmpp.farm.SpawnVm;

/**
 * User: marko
 * Date: Jul 16, 2009
 * Time: 10:06:21 PM
 */
public class SpawnVmListener implements PacketListener {
    // TODO: IS THIS CLASS REALLY NEEDED?
    protected VilleinGui villeinGui;

    public SpawnVmListener(VilleinGui villeinGui) {
        this.villeinGui = villeinGui;
    }

    public void processPacket(Packet packet) {
        SpawnVm spawnVm = (SpawnVm) packet;
        if (spawnVm.getType() == IQ.Type.ERROR) {
            villeinGui.updateHostAreaTree(spawnVm.getVmJid(), true);
        } else {
            villeinGui.updateHostAreaTree(spawnVm.getVmJid(), false);
        }
    }

}
