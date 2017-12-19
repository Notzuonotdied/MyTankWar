import java.awt.*;

public class BlockWall {
    // 定义墙的大小
    public final static int BlockWallwidth = 35;
    public final static int BlockWallheight = 36;
    // 初始化墙的图片
    private static Image[] wallImags = null;

    static {
        wallImags = new Image[]{Toolkit.getDefaultToolkit().getImage(
                BlockWall.class.getResource("/images/BlockWall.gif")),};
    }

    // 定义坐标
    int x;
    int y;
    // 定义普通墙的存在状态--无敌的
    boolean isLive = true;

    // 构造函数
    public BlockWall(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // 画出石墙
    public void drawBWall(Graphics g) {
        g.drawImage(wallImags[0], x, y, 35, 35, null);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, BlockWallheight, BlockWallwidth);
    }
}