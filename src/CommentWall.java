import java.awt.*;

public class CommentWall {
    // 定义墙的大小
    public final static int commentwallwidth = 22;
    public final static int commentwallheight = 21;
    // 初始化墙的图片
    private static Image[] wallImags = null;

    static {
        wallImags = new Image[]{
                Toolkit.getDefaultToolkit().getImage(
                        CommentWall.class.getResource("/images/commonWall.gif")),};
    }

    // 定义坐标
    int x;
    int y;
    // 定义普通墙的存在状态
    boolean isLive = true;

    // 构造函数
    public CommentWall(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // 画出普通墙
    public void drawCWall(Graphics g) {
        g.drawImage(wallImags[0], x, y, null);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, commentwallwidth, commentwallheight);
    }
}