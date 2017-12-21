import UIElement.BlockWall;
import UIElement.CommonWall;
import Util.Direction;

import static Util.CommonUtil.size;

// 我的坦克类
public class MyTank extends TankMember {
    // 定义坦克的速度
    private static final int speed = 7;
    // 定义坦克的生命值
    public static int blood = 200;

    MyTank(int x, int y) {
        super(x, y);
    }

    // 防止重叠
    public boolean isTouchOtherTank(Direction direct) {
        switch (this.direct) {
            case UP:
                for (int i = 0; i < Monster.getInstance().getMonsterSize(); i++) {
                    Monster mon = Monster.getInstance().getMonsterAt(i);
                    if (mon.direct == Direction.UP || mon.direct == Direction.DOWN) {
                        // 左点
                        if (this.x >= mon.x && this.x <= mon.x + size + 1
                                && this.y >= mon.y && this.y <= mon.y + size + 1) {
                            return true;
                        }
                        if (this.x + size + 1 >= mon.x
                                && this.x + size + 1 <= mon.x + size + 1
                                && this.y >= mon.y && this.y <= mon.y + size + 1) {
                            return true;
                        }
                    }
                    if (mon.direct == Direction.RIGHT || mon.direct == Direction.LEFT) {
                        if (this.x >= mon.x && this.x <= mon.x + size + 1
                                && this.y >= mon.y && this.y <= mon.y + size + 1) {
                            return true;
                        }
                        if (this.x + size + 1 >= mon.x
                                && this.x + size + 1 <= mon.x + size + 1
                                && this.y >= mon.y && this.y <= mon.y + size + 1) {
                            return true;
                        }
                    }
                }
                break;
            case RIGHT:
                // 取出所有的敌人坦克
                for (int i = 0; i < Monster.getInstance().getMonsterSize(); i++) {
                    // 取出第一个坦克
                    Monster mon = Monster.getInstance().getMonsterAt(i);
                    // 如果敌人的方向是向下或者向上
                    if (mon.direct == Direction.UP || mon.direct == Direction.DOWN) {
                        // 上点
                        if (this.x + size >= mon.x && this.x + size <= mon.x + size
                                && this.y >= mon.y && this.y <= mon.y + size) {
                            return true;
                        }
                        // 下点
                        if (this.x + size >= mon.x && this.x + size <= mon.x + size
                                && this.y + size >= mon.y
                                && this.y + size <= mon.y + size) {
                            return true;
                        }
                    }
                    if (mon.direct == Direction.LEFT || mon.direct == Direction.RIGHT) {
                        if (this.x + size >= mon.x && this.x + size <= mon.x + size
                                && this.y >= mon.y && this.y <= mon.y + size) {
                            return true;
                        }
                        if (this.x + size >= mon.x && this.x + size <= mon.x + size
                                && this.y + size >= mon.y
                                && this.y + size <= mon.y + size) {
                            return true;
                        }
                    }
                }
                break;
            case DOWN:
                // 取出所有的敌人坦克
                for (int i = 0; i < Monster.getInstance().getMonsterSize(); i++) {
                    // 取出第一个坦克
                    Monster mon = Monster.getInstance().getMonsterAt(i);
                    // 如果敌人的方向是向下或者向上
                    if (mon.direct == Direction.UP || mon.direct == Direction.DOWN) {
                        // 我的左点
                        if (this.x >= mon.x && this.x <= mon.x + size
                                && this.y + size >= mon.y
                                && this.y + size <= mon.y + size) {
                            return true;
                        }
                        // 我的右点
                        if (this.x + size >= mon.x && this.x + size <= mon.x + size
                                && this.y + size >= mon.y
                                && this.y + size <= mon.y + size) {
                            return true;
                        }
                    }
                    if (mon.direct == Direction.LEFT || mon.direct == Direction.RIGHT) {
                        if (this.x >= mon.x && this.x <= mon.x + size
                                && this.y + size >= mon.y
                                && this.y + size <= mon.y + size) {
                            return true;
                        }

                        if (this.x + size >= mon.x && this.x + size <= mon.x + size
                                && this.y + size >= mon.y
                                && this.y + size <= mon.y + size) {
                            return true;
                        }
                    }
                }

                break;
            case LEFT:
                // 向左
                // 取出所有的敌人坦克
                for (int i = 0; i < Monster.getInstance().getMonsterSize(); i++) {
                    // 取出第一个坦克
                    Monster mon = Monster.getInstance().getMonsterAt(i);
                    // 如果敌人的方向是向下或者向上
                    if (mon.direct == Direction.UP || mon.direct == Direction.DOWN) {
                        // 我的上一点
                        if (this.x >= mon.x && this.x <= mon.x + size
                                && this.y >= mon.y && this.y <= mon.y + size) {
                            return true;
                        }
                        // 下一点
                        if (this.x >= mon.x && this.x <= mon.x + size
                                && this.y + size >= mon.y
                                && this.y + size <= mon.y + size) {
                            return true;
                        }
                    }
                    if (mon.direct == Direction.LEFT || mon.direct == Direction.DOWN) {
                        // 上一点
                        if (this.x >= mon.x && this.x <= mon.x + size
                                && this.y >= mon.y && this.y <= mon.y + size) {
                            return true;
                        }
                        if (this.x >= mon.x && this.x <= mon.x + size
                                && this.y + size >= mon.y
                                && this.y + size <= mon.y + size) {
                            return true;
                        }
                    }
                }
                break;
        }
        return false;
    }

    // 定义我的坦克的移动函数
    public void move() {
        // 保存旧的位置
        set2OldDirect();
        switch (direct) { // 选择移动方向
            case UP:
                if (!isTouchOtherTank(direct) && !isTouchBorder()
                        && !isTouchCWall() && !isTouchBWall()) {
                    this.y -= speed;
                    if (isTouchOtherTank(direct) || isTouchBorder()
                            || isTouchCWall() || isTouchBWall()) {
                        this.chang2OldDirect();
                    }
                }
                break;
            case RIGHT:
                this.set2OldDirect();
                if (!isTouchOtherTank(direct) && !isTouchBorder()
                        && !isTouchCWall() && !isTouchBWall()) {
                    this.x += speed;
                    if (isTouchOtherTank(direct) || isTouchBorder()
                            || isTouchCWall() || isTouchBWall()) {
                        this.chang2OldDirect();
                    }
                }
                break;
            case DOWN:
                this.set2OldDirect();
                if (!isTouchOtherTank(direct) && !isTouchBorder()
                        && !isTouchCWall() && !isTouchBWall()) {
                    this.y += speed;
                    if (isTouchOtherTank(direct) || isTouchBorder()
                            || isTouchCWall() || isTouchBWall()) {
                        this.chang2OldDirect();
                    }
                }
                break;
            case LEFT:
                this.set2OldDirect();
                if (!isTouchOtherTank(direct) && !isTouchBorder()
                        && !isTouchCWall() && !isTouchBWall()) {
                    x -= speed;
                    if (isTouchOtherTank(direct) || isTouchBorder()
                            || isTouchCWall() || isTouchBWall()) {
                        this.chang2OldDirect();
                    }
                }
                break;
        }

    }

    public boolean isTouchCWall() {
        for (int i = 0; i < CommonWall.getInstance().getCWallsSize(); i++) {
            if (GamePanel.myTank.getRect().intersects(
                    CommonWall.getInstance().getCWallRectAt(i))) {
                return true;
            }
        }
        return false;
    }

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
