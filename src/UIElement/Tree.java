package UIElement;

import java.awt.*;
import java.util.Vector;

public class Tree {

    // 定义墙的大小
    private final static int TreeWidth = 36;
    private final static int TreeHeight = 36;
    private static Tree tree;
    // 初始化墙的图片
    private static Image[] treeImags = null;

    static {
        treeImags = new Image[]{Toolkit.getDefaultToolkit().getImage(
                Tree.class.getResource("/images/tree.gif")),};
    }

    // 定义坐标
    public int x;
    public int y;
    private Vector<Tree> trees = new Vector<>();

    private Tree() {
        initThee();
    }

    // 构造函数
    private Tree(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Tree getInstance() {
        if (tree == null) {
            synchronized (Tree.class) {
                if (tree == null) {
                    tree = new Tree();
                }
            }
        }
        return tree;
    }

    public void drawAllTree(Graphics graphics) {
        // 画出树木
        for (Tree aTree : trees) {
            aTree.drawTree(graphics);
        }
    }

    private void initThee() {
        // 初始化草丛
        for (int i = 0; i < 5; i++) {
            trees.add(new Tree(TreeWidth, TreeHeight * (5 + i)));
            trees.add(new Tree(TreeWidth * 2, TreeHeight * (5 + i)));
            trees.add(new Tree(TreeWidth * 19, TreeHeight * (5 + i)));
            trees.add(new Tree(TreeWidth * 20, TreeHeight * (5 + i)));
        }
    }

    // 画出普通墙
    private void drawTree(Graphics g) {
        g.drawImage(treeImags[0], x, y, null);
    }

}