package UIElement;

import java.awt.*;

public class Tree {

    // 定义墙的大小
    public final static int TreeWidth = 36;
    public final static int TreeHeight = 36;

    // 初始化墙的图片
    private static Image[] treeImags = null;

    static {
        treeImags = new Image[]{Toolkit.getDefaultToolkit().getImage(
                Tree.class.getResource("/images/tree.gif")),};
    }

    // 定义坐标
    public int x;
    public int y;

    // 定义普通墙的存在状态--永远存在的
    // boolean isLive = true;

    // 构造函数
    public Tree(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // 画出普通墙
    public void drawTree(Graphics g) {
        g.drawImage(treeImags[0], x, y, null);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, TreeWidth, TreeHeight);
    }
}