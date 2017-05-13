package com.unitalk.client;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

import com.unitalk.constants.ChatMessage;
import com.unitalk.constants.ClientDetails;
import com.unitalk.constants.MessageConstants;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class ClientController {

	@FXML
	private TextField message;
	@FXML
	private Button buttonSend;
	@FXML
	private ListView<String> userList;
	@FXML
	private Text serverIp;
	@FXML
	private Text userName;
	@FXML
	private TextArea clientLogs;
	@FXML
	private Button logoutButton;
	@FXML
	private Button btnLine;
	@FXML
	private Button btnCircle;
	@FXML
	private Button btnSquare;
	@FXML
	private Button btnRectangle;
	@FXML
	private Button btnText;
	@FXML
	private Button btnBrush;
	@FXML
	private Button btnZoom;
	@FXML
	private Button btnPencil;
	@FXML
	private Button btnRotateLeft;
	@FXML
	private Button btnRotateRight;
	@FXML
	private Button btnClear;
	@FXML
	private Button btnImage;
	@FXML
	private ColorPicker colorPicker;
	@FXML
	private StackPane stackPane;
	@FXML
	public Canvas canvas;

	private Socket serverSocket;
	public  ObjectInputStream ois;
	public  ObjectOutputStream oos;
	private ClientDetails clientdetails;
	public static String setTool;
	private GraphicsContext gc;
	private double x, y;
	private Image screenShot;
	private Image importImage;
	final FileChooser fileChooser = new FileChooser();
	@FXML
	public void initialize() {
		try {
			this.userName.setText(ClientMain.controller.getButtonLogin().getText());
			System.out.println(this.userName);
			setTool = "na";
			gc = canvas.getGraphicsContext2D();
			colorPicker.setValue(Color.BLACK);
			this.serverIp.setText(ClientMain.controller.getServerId().getText());
			if (serverIp.getText() != null && !serverIp.getText().isEmpty()) {
				this.serverSocket = new Socket(this.serverIp.getText(), 9888);
			} else {
				this.serverIp.setText("Localhost");
				this.serverSocket = new Socket("localhost", 9888);
			}
			this.clientLogs.appendText("Connected to Server :" + serverSocket.getInetAddress() + " Port :"
					+ serverSocket.getPort() + "\n");
			this.ois = new ObjectInputStream(this.serverSocket.getInputStream());
			this.oos = new ObjectOutputStream(this.serverSocket.getOutputStream());
			RunnableClient runnableClient = new RunnableClient(this, this.serverSocket, ois, oos);
			Thread runnableClientThread = new Thread(runnableClient);
			runnableClientThread.start();

		} catch (UnknownHostException e) {
			this.clientLogs.appendText("Unknown host error\n");
			e.printStackTrace();
		} catch (IOException e) {
			this.clientLogs.appendText("Internal server error\n");
			e.printStackTrace();
		}
		finally {
			registerClient();
		}

	}
	
	@FXML
	void btnLineAction (ActionEvent event){
		setTool = "line";
	}
	
	@FXML
	void btnPencilAction (ActionEvent event){
		setTool = "pencil";
	}
	
	@FXML
	void btnBrushAction (ActionEvent event){
		setTool = "brush";
	}
	
	@FXML
	void btnRectangleAction (ActionEvent event){
		setTool = "rectangle";
	}
	
	@FXML
	void btnSquareAction (ActionEvent event){
		setTool = "square";
	}
	
	@FXML
	void btnCircleAction (ActionEvent event){
		setTool = "circle";
	}
	
	@FXML
	void btnEllipseAction (ActionEvent event){
		setTool = "ellipse";
	}
	
	@FXML
	void btnRotateLeftAction (ActionEvent event){
		
		canvas.setRotate(canvas.getRotate()-90);

	}
	
	@FXML
	void btnRotateRightAction (ActionEvent event){
		
		canvas.setRotate(canvas.getRotate()+90);
		
		
	}
	
	@FXML
	void colorPickerAction (ActionEvent event){
		System.out.println(colorPicker.getValue());
		gc.setStroke(colorPicker.getValue());
	}
	
	
	@FXML
	void btnZoomPlusClick (MouseEvent event){
		
		canvas.setScaleX(canvas.getScaleX() * 1.05);
		canvas.setScaleY(canvas.getScaleY() * 1.05);
		event.consume();
	}
	
	@FXML
	void btnZoomMinusClick (MouseEvent event){
		
		canvas.setScaleX(canvas.getScaleX() * 0.95);
		canvas.setScaleY(canvas.getScaleY() * 0.95);
		event.consume();
	}
	
	@FXML
	void btnClearAction (ActionEvent event){
		
		gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
	}
	
	@FXML
	void btnImageAction (ActionEvent event){
		
		configureFileChooser(fileChooser);
          File file = fileChooser.showOpenDialog(ClientMain.currentStage);
          if (file != null) {
              try {
            	  importImage = new Image(file.toURI().toString());
            	  setTool = "ImageTool";
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
          }
          else {
        	  importImage = null;
          }
		
	}
	
	
	@FXML
	void canvasOnScroll (ScrollEvent event){
		double zoomFactor = 1.05;
        double deltaY = event.getDeltaY();
        if (deltaY < 0){
          zoomFactor = 2.0 - zoomFactor;
        }
        canvas.setScaleX(canvas.getScaleX() * zoomFactor);
        canvas.setScaleY(canvas.getScaleY() * zoomFactor);
        event.consume();
	}
	
	@FXML
	void canvasHover (MouseEvent event ){
		if (setTool != "na"){
			switch(setTool){
				case "pencil":
								Image cursorImage = new Image ("com/unitalk/client/icons/pencil.png");
								canvas.setCursor(new ImageCursor(cursorImage, cursorImage.getWidth()/2, cursorImage.getHeight()/2));
								break;
				case "brush":
								Image cursorImageBrush = new Image ("com/unitalk/client/icons/brush.png");
								canvas.setCursor(new ImageCursor(cursorImageBrush, cursorImageBrush.getWidth()/2, cursorImageBrush.getHeight()/2));
								break;
				case "ImageTool":
								Image cursorImageImport = new Image ("com/unitalk/client/icons/image.png");
								canvas.setCursor(new ImageCursor(cursorImageImport, cursorImageImport.getWidth()/2, cursorImageImport.getHeight()/2));
								break;
				default:	canvas.setCursor(Cursor.CROSSHAIR);
			}
		}
	}
	
	@FXML
	void canvasHoverExit (MouseEvent event){
		canvas.setCursor(Cursor.DEFAULT);
	}
	
	@FXML
	void canvasMousePressed (MouseEvent event){
			switch(setTool){
			case "line": 
						gc.beginPath();
				        gc.moveTo(event.getX(), event.getY());
				        gc.stroke();
						break;
			case "pencil":
							gc.beginPath();
					        gc.moveTo(event.getX(), event.getY());
					        gc.setStroke(colorPicker.getValue());
					        gc.setLineWidth(1.0);
					        gc.stroke();
					      break;
			case "brush":
							gc.beginPath();
					        gc.moveTo(event.getX(), event.getY());
					        gc.setStroke(colorPicker.getValue());
					        gc.setLineWidth(3.0);
					        gc.stroke();
					      break;
			case "rectangle": 
								x = event.getX();
								y = event.getY();
						        break;
			case "square": 
							x = event.getX();
							y = event.getY();
					        break;
			case "circle": 
							x = event.getX();
							y = event.getY();
					        break;
			case "ellipse": 
							x = event.getX();
							y = event.getY();
					        break;
			}
	}
	
	@FXML
	void canvasMouseDrag(MouseEvent event){
		switch(setTool){
		case "pencil":
					 gc.lineTo(event.getX(), event.getY());
					 gc.setStroke(colorPicker.getValue());
			         gc.stroke();
			         sendScreenShot();
			         break;
		case "brush":
					 gc.lineTo(event.getX(), event.getY());
					 gc.setStroke(colorPicker.getValue());
			         gc.stroke();
			         sendScreenShot();
			         break;	   

		}
	}
	
	@FXML
	void canvasMouseRelease (MouseEvent event){
			switch(setTool){
			case "line":
						 gc.lineTo(event.getX(), event.getY());
				         gc.stroke();
				         sendScreenShot();
				         break;
			case "rectangle": 
							gc.strokeRect(x, y, event.getX() - x, event.getY() - y);
							sendScreenShot();
					        break;
													
			case "square": 
							gc.strokeRect(x, y, event.getY() - y, event.getY() - y);
							sendScreenShot();
					        break;
			case "circle":
							gc.strokeArc(x, y, event.getY()-y, event.getY()-y, 0, 360, ArcType.OPEN);
							sendScreenShot();
							break;
			case "ellipse":
							gc.strokeArc(x, y, event.getX()-x, event.getY()-y, 0, 360, ArcType.OPEN);
							sendScreenShot();
							break;
			case "ImageTool":
							gc.drawImage(importImage, event.getX(), event.getY(), 250, 200);
							sendScreenShot();
							break;
			}
	}

	@FXML
	void buttonSendAction(ActionEvent event) {

		try {
			ChatMessage newMessage= new ChatMessage();
			if(this.userList.getSelectionModel().getSelectedItem().isEmpty())
			{
				this.getUserList().getSelectionModel().select("Broadcast");
			}

			if(!this.userList.getSelectionModel().getSelectedItem().equals("Broadcast"))
			{
				newMessage.setMessageType(MessageConstants.PRIVATE_MESSAGE);
				newMessage.setRecipientClient(this.userList.getSelectionModel().getSelectedItem());
				this.clientLogs.appendText(this.userName.getText()+" to "+this.userList.getSelectionModel().getSelectedItem()+">"+this.message.getText()+"\n");
			}
			else
			{
				newMessage.setMessageType(MessageConstants.CHAT_BROADCAST);
				this.clientLogs.appendText(this.userName.getText()+">"+this.message.getText()+"\n");
			}

			newMessage.setMessage(this.message.getText());
			newMessage.setClientDetails(clientdetails);

			oos.writeObject(newMessage);
		} catch (IOException e) {
			this.clientLogs.appendText("Internal server error\n");
			e.printStackTrace();
		}
	}

	@FXML
	void buttonLogoutAction(ActionEvent event) {
		try {
			ChatMessage exitMessage= new ChatMessage();
			exitMessage.setMessageType(MessageConstants.EXIT_MESSAGE);
			exitMessage.setMessage(this.getUserName().getText());
			oos.writeObject(exitMessage);
			this.userList.getItems().remove(exitMessage.getMessage());
			Platform.exit();
		} catch (IOException e) {
			this.clientLogs.appendText("Internal server error\n");
			e.printStackTrace();
		}
	}

// register client method
	public void registerClient(){
		try {
			ChatMessage registerMessage= new ChatMessage();
			registerMessage.setMessageType(MessageConstants.REGISTER_CLIENT);
			registerMessage.setMessage(this.getUserName().getText());
			this.clientdetails= new ClientDetails();
			this.clientdetails.setNickname(this.userName.getText());
			oos.writeObject(registerMessage);
		} catch (IOException e) {
			this.clientLogs.appendText("Internal server error\n");
			e.printStackTrace();
		}
	}


	public Socket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(Socket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public  ClientDetails getClientdetails() {
		return clientdetails;
	}

	public  void setClientdetails(ClientDetails clientdetails) {
		this.clientdetails = clientdetails;
	}


	public TextField getMessage() {
		return message;
	}

	public void setMessage(TextField message) {
		this.message = message;
	}

	public Button getButtonSend() {
		return buttonSend;
	}

	public void setButtonSend(Button buttonSend) {
		this.buttonSend = buttonSend;
	}

	public ListView<String> getUserList() {
		return userList;
	}

	public void setUserList(ListView<String> userList) {
		this.userList = userList;
	}

	public Text getServerIp() {
		return serverIp;
	}

	public void setServerIp(Text serverIp) {
		this.serverIp = serverIp;
	}

	public Text getUserName() {
		return userName;
	}

	public void setUserName(Text userName) {
		this.userName = userName;
	}

	public TextArea getClientLogs() {
		return clientLogs;
	}

	public void setClientLogs(TextArea clientLogs) {
		this.clientLogs = clientLogs;
	}

	public Button getLogoutButton() {
		return logoutButton;
	}

	public void setLogoutButton(Button logoutButton) {
		this.logoutButton = logoutButton;
	}
	
	public Image getscreenShot(){
		return screenShot;
	}
	public void setScreenShot(Image screenShot){
		this.screenShot = screenShot;
	}
	
	// send sceenshot method
	public void sendScreenShot(){
		SnapshotParameters params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
		this.screenShot = canvas.snapshot(params, null);
		ChatMessage newMessage= new ChatMessage();
		
			if (!this.userList.getItems().isEmpty()){
						if(this.userList.getSelectionModel().getSelectedItem().isEmpty())
						{
							this.getUserList().getSelectionModel().select("Broadcast");
						}
			
						if(!this.userList.getSelectionModel().getSelectedItem().equals("Broadcast"))
						{
							newMessage.setMessageType(MessageConstants.PRIVATE_MESSAGE);
							newMessage.setRecipientClient(this.userList.getSelectionModel().getSelectedItem());
						}
						else
						{
							newMessage.setMessageType(MessageConstants.CHAT_BROADCAST);
						}
			
						newMessage.setMessage("Drawing");
						newMessage.setByteArray(imageToBytes(screenShot));
						newMessage.setClientDetails(clientdetails);			
						try {
							oos.writeObject(newMessage);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
				}
			}
	}
	
	// method to configure file chooser
	private void configureFileChooser(FileChooser fileChooser) {
			
			fileChooser.setTitle("Import an Image");
          fileChooser.setInitialDirectory(
              new File(System.getProperty("user.home"))
          );                 
          fileChooser.getExtensionFilters().addAll(
              new FileChooser.ExtensionFilter("Images", "*.jpg*", "*.png"),
              new FileChooser.ExtensionFilter("JPEG", "*.jpg"),
              new FileChooser.ExtensionFilter("PNG", "*.png")
          );	
		}
	
	// method to convert images to bytes which is suitable for serialization
	public byte[] imageToBytes (Image screenShot){
		ByteArrayOutputStream  byteOutput = new ByteArrayOutputStream();
		byte[] imageBytes = null;
		try {
			ImageIO.write( SwingFXUtils.fromFXImage( screenShot, null ), "png", byteOutput );
			imageBytes = byteOutput.toByteArray();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return imageBytes;
	}
	

}
