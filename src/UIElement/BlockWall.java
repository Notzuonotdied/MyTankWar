package UIElement;

import java.awt.*;
import java.util.Vector;

import static Util.CommonUtil.screenHeight;
import static Util.CommonUtil.screenWidth;

public class BlockWall {

    // 定义墙的大小
    public static final int BlockWallWidth = 35;
    public static final int BlockWallHeight = 36;
    // 定义一个内部石墙集合
    private static Vector<BlockWall> BWalls_1 = new Vector<>();
    // 初始化墙的图片
    private static Image[] wallImages;
    private static BlockWall blockWall;

    static {
        wallImages = new Image[]{Toolkit.getDefaultToolkit().getImage(
                BlockWall.class.getResource("/images/BlockWall.gif")),};
    }

    private Vector<BlockWall> BWalls = new Vector<>();
    // 定义坐标
    private int x;
    private int y;

    private BlockWall() {
        initBWall();
    }

    // 构造函数
    private BlockWall(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static BlockWall getInstance() {
        if (blockWall == null) {
            synchronized (BlockWall.class) {
                if (blockWall == null) {
                    blockWall = new BlockWall();
                }
            }
        }
        return blockWall;
    }

    public int getBWalls_1Size() {
        return BWalls_1.size();
    }

    public void drawAllBWall(Graphics graphics) {
        // 画出外围石墙
        for (BlockWall BWall : this.BWalls) {
            BWall.drawBWall(graphics);
        }
        // 内部石墙
        for (BlockWall aBWalls_1 : BWalls_1) {
            aBWalls_1.drawBWall(graphics);
        }
    }

    private void initBWall() {
        // 初始化除了普通墙外的所有墙体--注：其他的墙体都设置为无敌了，不可攻击
        // 初始化石墙-外部围墙
        for (int i = 0; i < 23; i++) {
            BWalls.add(new BlockWall(BlockWallWidth * i, 0));
            BWalls.add(new BlockWall(BlockWallWidth * i,
                    screenHeight - BlockWallHeight));
            BWalls.add(new BlockWall(0, BlockWallHeight * i));
            BWalls.add(new BlockWall(screenWidth - BlockWallWidth,
                    BlockWallHeight * i));
        }
        // 初始化石墙-内部围墙
        for (int i = 0; i < 4; i++) {
            BWalls_1.add(new BlockWall(BlockWallWidth * (i + 6),
                    BlockWallHeight * 5));
            BWalls_1.add(new BlockWall(BlockWallWidth * (i + 13),
                    BlockWallHeight * 5));
        }
        // 初始化石墙-内部墙-上两个
        for (int i = 0; i < 5; i++) {
            if (i < 2) {
                BWalls_1.add(new BlockWall(BlockWallWidth * (i + 5),
                        BlockWallHeight * 10));
                BWalls_1.add(new BlockWall(BlockWallWidth * (i + 16),
                        BlockWallHeight * 10));
                BWalls_1.add(new BlockWall(BlockWallWidth * (i + 5),
                        BlockWallHeight * 11));
                BWalls_1.add(new BlockWall(BlockWallWidth * (i + 16),
                        BlockWallHeight * 11));
            } else {
                BWalls_1.add(new BlockWall(BlockWallWidth * (i + 8),
                        BlockWallHeight * 10));
            }
        }
        // 初始化石墙-内部墙-最下一个
        for (int i = 0; i < 5; i++) {
            if (i == 0 || i == 4) {
                BWalls_1.add(new BlockWall(BlockWallWidth * (i + 9),
                        BlockWallHeight * 13));
            } else {
                BWalls_1.add(new BlockWall(BlockWallWidth * (i + 9),
                        BlockWallHeight * 14));
            }
        }
        // 初始化石墙-内部墙-左右两个
        for (int i = 0; i < 2; i++) {
            BWalls_1.add(new BlockWall(BlockWallWidth * 3,
                    BlockWallHeight * (i + 7)));
            BWalls_1.add(new BlockWall(BlockWallWidth * 19,
                    BlockWallHeight * (i + 7)));
            BWalls_1.add(new BlockWall(BlockWallWidth * 6,
                    BlockWallHeight * (i + 16)));
            BWalls_1.add(new BlockWall(BlockWallWidth * 16,
                    BlockWallHeight * (i + 16)));
        }
    }

    public Rectangle getBWallRectAt(int index) {
        return BlockWall.BWalls_1.get(index).getRect();
    }

    /**
     * 画出石墙
     *
     * @param g 画笔
     */
    private void drawBWall(Graphics g) {
        g.drawImage(wallImages[0], x, y, BlockWallWidth, BlockWallHeight, null);
    }

    private Rectangle getRect() {
        return new Rectangle(x, y, BlockWallHeight, BlockWallWidth);
    }
}