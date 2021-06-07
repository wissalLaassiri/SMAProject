package agents.seller.copy;

import agents.buyer.BookBuyerContainer;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

public class SellerAgent extends GuiAgent {

	protected SellerContainer gui;
	@Override
	protected void setup() {
		if (getArguments().length == 1) {
			gui = (SellerContainer) getArguments()[0];
			gui.sellerAgent = this;
		}
		ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
		addBehaviour(parallelBehaviour);
		parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {

			@Override
			public void action() {
				ACLMessage aclMessage = receive();
				if (aclMessage != null) {
					gui.logMesssage(aclMessage);
					ACLMessage reply = aclMessage.createReply();
					reply.setPerformative(ACLMessage.INFORM);
					reply.setContent(" seller :  "+aclMessage.getContent());
					send(reply);
				} else {
					block();
				}
			}
		});

	}
	@Override
	protected void onGuiEvent(GuiEvent arg0) {
		
	}
	
}
