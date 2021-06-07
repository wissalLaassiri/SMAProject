package agents;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.ControllerException;

public class ConsumerAgent extends GuiAgent {

	protected ConsumerContainer consumerContainer;

	@Override
	protected void setup() {
		String bookName = null;
		if (this.getArguments().length == 1) {
			consumerContainer = (ConsumerContainer) getArguments()[0];
			consumerContainer.consumerAgent=this;
		}
		System.out.println("Initialisation de l'agent" + this.getAID().getName());
		System.out.println("I'm trying to buy the book " + bookName);

		ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
		addBehaviour(parallelBehaviour);
		/*
		 * parallelBehaviour.addSubBehaviour(new Behaviour() { private int counter = 0;
		 * 
		 * @Override public boolean done() { // TODO Auto-generated method stub return
		 * (counter==10); }
		 * 
		 * @Override public void action() {
		 * System.out.println("========================="); System.out.println("Step " +
		 * counter); System.out.println("========================="); ++counter; } });
		 */

		parallelBehaviour.addSubBehaviour(new OneShotBehaviour() {

			@Override
			public void action() {
				System.out.println("one shot ");

			}
		});

		parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
			// traiter seulement les message de langue francais et de type cfp
			
			@Override
			public void action() {
				MessageTemplate messageTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.CFP),
						MessageTemplate.MatchLanguage("fr"));
				ACLMessage aclMessage = receive();
				if (aclMessage != null) {
					System.out.println("Sender " + aclMessage.getSender().getName());
					System.out.println("Content " + aclMessage.getContent());
					System.out.println("Speech act " + aclMessage.getPerformative(aclMessage.getPerformative()));
					/*
					 * ACLMessage reply = new ACLMessage(ACLMessage.CONFIRM);
					 * reply.addReceiver(aclMessage.getSender()); reply.setContent("price = 900 ");
					 * send(reply);
					 */
					consumerContainer.logMesssage(aclMessage);
				} else {
					System.out.println("Bloccccc ------- ");
					block();
				}
			}
		});

		// ***********************************************8
		/*
		 * parallelBehaviour.addSubBehaviour(new TickerBehaviour(this, 1000) {
		 * 
		 * @Override protected void onTick() { System.out.println("tic");
		 * System.out.println(myAgent.getAID().getLocalName()); } });
		 */

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy:HH:mm");
		Date date = null;
		try {
			date = dateFormat.parse("21/05/2021:12:37");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		parallelBehaviour.addSubBehaviour(new WakerBehaviour(this, date) {
			@Override
			protected void onWake() {
				System.out.println("wake  ");
			}
		});
	}

	@Override
	protected void beforeMove() {
		try {
			System.out.println("Before Move" + this.getContainerController().getContainerName());
			System.out.println("=========================");
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void afterMove() {
		try {
			System.out.println("After Migration to " + this.getContainerController().getContainerName());
			System.out.println("=========================");
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void takeDown() {
		System.out.println("im going to die");
		System.out.println("=========================");
	}

	@Override
	protected void onGuiEvent(GuiEvent event) {
		if(event.getType()==1) {
			String bookName = (String) event.getParameter(0);
			ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
			aclMessage.setContent(bookName);
			aclMessage.addReceiver(new AID("BookBuyerAgent ",AID.ISLOCALNAME));
			send(aclMessage);

		}
	}

}
