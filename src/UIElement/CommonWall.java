package UIElement;

import java.awt.*;

public class CommonWall {
    // 定义墙的大小
    public static int commonWallWidth = 22;
    public static int commonWallHeight = 21;
    // 初始化墙的图片
    private static Image[] wallImages;

    static {
        wallImages = new Image[]{
                Toolkit.getDefaultToolkit().getImage(
                        CommonWall.class.getResource("/images/commonWall.gif")),};
    }

    // 定义普通墙的存在状态
    public boolean isLive = true;
    // 定义坐标
    private int x;
    private int y;
    // 构造函数
    public CommonWall(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // 画出普通墙
    public void drawCWall(Graphics g) {
        g.drawImage(wallImages[0], x, y, null);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, commonWallWidth, commonWallHeight);
    }
}