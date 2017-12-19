public class Main {

    public static void main(String[] args) {
        System.out.println("开始启动游戏！");
        // 实例化
        MainFrame mainFrame = new MainFrame();
        // 启动线程
        Thread t = new Thread(mainFrame::start);
        t.start();
    }

}