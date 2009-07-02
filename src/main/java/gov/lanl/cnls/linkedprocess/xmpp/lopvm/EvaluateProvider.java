package gov.lanl.cnls.linkedprocess.xmpp.lopvm;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;
import gov.lanl.cnls.linkedprocess.LinkedProcess;

/**
 * User: marko
 * Date: Jun 24, 2009
 * Time: 11:29:58 AM
 */
public class EvaluateProvider implements IQProvider {

    public IQ parseIQ(XmlPullParser parser) throws Exception {
        Evaluate evaluate = new Evaluate();

        String vmPassword = parser.getAttributeValue(LinkedProcess.BLANK_NAMESPACE, LinkedProcess.VM_PASSWORD_ATTRIBUTE);
        if(null != vmPassword) {
            evaluate.setVmPassword(vmPassword);
        }

        int v = parser.next();
        if(v == XmlPullParser.TEXT) {
            evaluate.setExpression(parser.getText());
            parser.next();
        }
        return evaluate;
    }
}
