package agents.seller.copy;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SellerContainer extends Application {
	protected SellerAgent sellerAgent;
	ObservableList<String> observableListData;
	AgentContainer container;
	public static void main(String[] args) throws Exception {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		startContainer();
		primaryStage.setTitle("Seller Container");
		BorderPane borderPane = new BorderPane();
		
		HBox hBox1 = new HBox();
		hBox1.setPadding(new Insets(10));
		hBox1.setSpacing(10);
		Label labelAgentName = new Label("Agent Name :");
		TextField textFieldAgentName = new TextField();
		Button buttonDep = new Button("Deploy");

		hBox1.getChildren().addAll(labelAgentName,textFieldAgentName,buttonDep);
		borderPane.setTop(hBox1);
		observableListData = FXCollections.observableArrayList();
		ListView<String> listViewMessages = new ListView<String>(observableListData);
		
		VBox vBox2 = new VBox();
		vBox2.setPadding(new Insets(10));
		vBox2.setSpacing(10);
		vBox2.getChildren().add(listViewMessages);
		
		buttonDep.setOnAction(evt->{
			try {
				String name = textFieldAgentName.getText();
				AgentController sellerController = container.createNewAgent(name, SellerAgent.class.getName(), new Object[] {this});
				sellerController.start();
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
 
		borderPane.setCenter(vBox2);
		Scene scene = new Scene(borderPane,600,400);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	private void startContainer() throws Exception {
		Runtime runtime = Runtime.instance();
		ProfileImpl profile = new ProfileImpl();
		profile.setParameter(Profile.MAIN_HOST, "localhost");
		container = runtime.createAgentContainer(profile);
		container.start();		
	}
	public void logMesssage(ACLMessage aclMessage) {
		Platform.runLater(()->{
			observableListData.add(aclMessage.getSender().getName() + " =>" + aclMessage.getContent());
		});
	}
}
