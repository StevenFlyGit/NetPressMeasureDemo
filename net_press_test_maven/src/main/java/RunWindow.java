import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
* 创建时间：2020年12月1日
* 启动窗口的入口方法
* @author LLB
*/
public class RunWindow extends Application{

	
	@Override
	public void start(Stage primaryStage) throws Exception {
		 
		//加载fxml文件，获根元素
		Parent root = FXMLLoader.load(this.getClass().getResource("/com/llb/view/TestView.fxml"));
		//定义一个画布对象
		Scene scene = new Scene(root);
		primaryStage.setTitle("测试时间窗口");
		primaryStage.setScene(scene);
		//展现窗口
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
