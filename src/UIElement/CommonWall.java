package UIElement;

import Util.CommonUtil;

import java.awt.*;
import java.util.Vector;

public class CommonWall {
    // 定义墙的大小
    private static int commonWallWidth = 22;
    private static int commonWallHeight = 21;
    // 初始化墙的图片
    private static Image[] wallImages;
    private static CommonWall commonWall;
    private static Vector<CommonWall> CWalls = new Vector<>();

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

    private CommonWall() {
        initBWall();
    }

    // 构造函数
    public CommonWall(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static CommonWall getInstance() {
        if (commonWall == null) {
            synchronized (CommonWall.class) {
                if (commonWall == null) {
                    commonWall = new CommonWall();
                }
            }
        }
        return commonWall;
    }

    public int getCWallsSize() {
        return CWalls.size();
    }

    public CommonWall getCWallAt(int index) {
        return CWalls.get(index);
    }

    public void add2CWalls(CommonWall commonWall) {
        CWalls.add(commonWall);
    }

    public void removeCWall(int index) {
        CWalls.remove(CommonWall.getInstance().getCWallAt(index));
    }

    public Rectangle getCWallRectAt(int index) {
        return CWalls.get(index).getRect();
    }

    public void drawAllCWall(Graphics g) {
        // 画出普通墙
        for (int i = 0; i < CWalls.size(); i++) {
            CommonWall cWall = CWalls.get(i);
            // 如果普通墙存在状态为真就画出来
            if (cWall.isLive) {
                cWall.drawCWall(g);
            } else {
                CWalls.remove(cWall);
            }
        }
    }

    /**
     * 初始化普通墙,可以被子弹击碎
     */
    public void initBWall() {
        CWalls.clear();
        // 初始化普通墙
        for (int i = 0; i < 15; i++) {
            if (i < 12) {
                CWalls.add(new CommonWall(commonWallWidth * (i + 4), 158));
                CWalls.add(new CommonWall(commonWallWidth * (i + 20), 158));
            }
            CWalls.add(new CommonWall(154, commonWallHeight * (i + 8)));
            CWalls.add(new CommonWall(632, commonWallHeight * (i + 8)));
        }

        for (int i = 0; i < 3; i++) {
            CWalls.add(new CommonWall(commonWallWidth * (i + 13), 215));
            CWalls.add(new CommonWall(commonWallWidth * (i + 13), 237));
            CWalls.add(new CommonWall(commonWallWidth * (i + 21), 215));
            CWalls.add(new CommonWall(commonWallWidth * (i + 21), 237));
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // 画出普通墙
    private void drawCWall(Graphics g) {
        g.drawImage(wallImages[0], x, y, null);
    }

    private Rectangle getRect() {
        return new Rectangle(x, y, commonWallWidth, commonWallHeight);
    }
}