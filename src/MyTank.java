import UIElement.BlockWall;
import UIElement.CommonWall;

// 我的坦克类
public class MyTank extends TankMember {
    // 定义坦克的速度
    private static final int speed = 7;
    // 定义坦克的生命值
    public static int blood = 200;

    MyTank(int x, int y) {
        super(x, y);
    }

    /**
     * 防止重叠
     */
    @Override
    public boolean isTouchOtherTank() {
        // 取出所有的敌人坦克
        for (int i = 0; i < Monster.getInstance().getMonsterSize(); i++) {
            // 取出第一个坦克
            Monster mon = Monster.getInstance().getMonsterAt(i);
            if (mon.getRect().intersects(GamePanel.myTank.getRect())) {
                return true;
            }
        }
        return false;
    }

    // 定义我的坦克的移动函数
    public void move() {
        // 保存旧的位置
        set2OldDirect();
        // 选择移动方向
        switch (direct) {
            case UP:
                if (canMove()) {
                    this.y -= speed;
                    this.afterMoveIsOkay();
                }
                break;
            case RIGHT:
                if (canMove()) {
                    this.x += speed;
                    this.afterMoveIsOkay();
                }
                break;
            case DOWN:
                if (canMove()) {
                    this.y += speed;
                    this.afterMoveIsOkay();
                }
                break;
            case LEFT:
                if (canMove()) {
                    x -= speed;
                    this.afterMoveIsOkay();
                }
                break;
        }
    }

    @Override
    public boolean isTouchCWall() {
        for (int i = 0; i < CommonWall.getInstance().getCWallsSize(); i++) {
            if (GamePanel.myTank.getRect().intersects(
                    CommonWall.getInstance().getCWallRectAt(i))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isTouchBWall() {
        for (int i = 0; i < BlockWall.getInstance().getBWalls_1Size(); i++) {
            if (GamePanel.myTank.getRect().intersects(
                    BlockWall.getInstance().getBWallRectAt(i))) {
                return true;
            }
        }
        return false;
    }
}

