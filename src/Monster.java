import UIElement.BlockWall;
import UIElement.CommonWall;
import Util.Audio;
import Util.CommonUtil;
import Util.Direction;

import java.util.Random;

// 怪物类
// 由于怪物是多个的，且要求互不影响，所以要做成多线程的
public class Monster extends TankMember implements Runnable {
    // 定义怪物的停顿时间
    private static final int WaitingTime = 25;
    // 定义让坦克随机产生一个随机的新的方向的等待时间
    private static final int WaitingForRandom = 30;
    // 定义坦克类型
    private static final int type = 1;
    // 定义坦克的生命值
    public static int blood = 50;
    // 定义怪物连发子弹的数量
    static int bulletOneTime = 2;
    // 定义怪物的数量
    private static int TankNumber = 0;
    // 定义随机变量
    private static Random r = new Random();
    private int movesteps = r.nextInt(30) + 15;

    // 调用父类的构造函数进行初始化
    public Monster(int x, int y) {
        super(x, y);
    }

    // 以下为成员变量的构造函数
    public static int getTankNumber() {
        return TankNumber;
    }

    public static void setTankNumber(int tankNumber) {
        TankNumber = tankNumber;
    }

    // 定义我的坦克的移动函数
    public void move() {
        // 保存旧的位置,保证出现错误的移动可以恢复到原先的位置
        set2OldDirect();

        switch (direct) { // 选择移动方向
            case UP:
                y -= speed;
                break;
            case RIGHT:
                x += speed;
                break;
            case DOWN:
                y += speed;
                break;
            case LEFT:
                x -= speed;
                break;
        }
    }

    // 防止重叠
    // public boolean isTouchOtherTank(int direct) {
    // for (int i = 0; i < GamePanel.monster.size(); i++) {
    // Monster mon = GamePanel.monster.get(i);
    // if (this != mon) {
    // if (mon.getRect().intersects(this.getRect())) {
    // return true;
    // }
    // }
    // }
    // return false;
    // }
    //
    // public boolean isTouchMyTank(Monster mon) {
    // if (mon.getRect().intersects(GamePanel.myTank.getRect())) {
    // return true;
    // }
    // return false;
    // }
    public boolean isTouchOtherTank(Direction direct) {
        switch (this.direct) {
            case UP:
                for (int i = 0; i < GamePanel.monster.size(); i++) {
                    Monster mon = GamePanel.monster.get(i);
                    if (this != mon) {
                        if (mon.direct == Direction.UP || mon.direct == Direction.DOWN) {
                            // 左点
                            if (this.x >= mon.x && this.x <= mon.x + size + 1
                                    && this.y >= mon.y
                                    && this.y <= mon.y + size + 1) {
                                return true;
                            }
                            if (this.x + size + 1 >= mon.x
                                    && this.x + size + 1 <= mon.x + size + 1
                                    && this.y >= mon.y
                                    && this.y <= mon.y + size + 1) {
                                return true;
                            }
                        }
                        if (mon.direct == Direction.LEFT || mon.direct == Direction.RIGHT) {
                            if (this.x >= mon.x && this.x <= mon.x + size + 1
                                    && this.y >= mon.y
                                    && this.y <= mon.y + size + 1) {
                                return true;
                            }
                            if (this.x + size + 1 >= mon.x
                                    && this.x + size + 1 <= mon.x + size + 1
                                    && this.y >= mon.y
                                    && this.y <= mon.y + size + 1) {
                                return true;
                            }
                        }
                    }
                    if (type == 1) {
                        if (GamePanel.myTank.direct == Direction.UP || GamePanel.myTank.direct == Direction.DOWN) {
                            // 左点
                            if (this.x >= GamePanel.myTank.x
                                    && this.x <= GamePanel.myTank.x + size + 1
                                    && this.y >= GamePanel.myTank.y
                                    && this.y <= GamePanel.myTank.y + size + 1) {
                                return true;
                            }
                            if (this.x + size + 1 >= GamePanel.myTank.x
                                    && this.x + size + 1 <= GamePanel.myTank.x
                                    + size + 1
                                    && this.y >= GamePanel.myTank.y
                                    && this.y <= GamePanel.myTank.y + size + 1) {
                                return true;
                            }
                        }
                        if (GamePanel.myTank.direct == Direction.LEFT || GamePanel.myTank.direct == Direction.RIGHT) {
                            if (this.x >= GamePanel.myTank.x
                                    && this.x <= GamePanel.myTank.x + size + 1
                                    && this.y >= GamePanel.myTank.y
                                    && this.y <= GamePanel.myTank.y + size + 1) {
                                return true;
                            }
                            if (this.x + size + 1 >= GamePanel.myTank.x
                                    && this.x + size + 1 <= GamePanel.myTank.x
                                    + size + 1
                                    && this.y >= GamePanel.myTank.y
                                    && this.y <= GamePanel.myTank.y + size + 1) {
                                return true;
                            }
                        }
                    }
                }
            case RIGHT:
                // 取出所有的敌人坦克
                for (int i = 0; i < GamePanel.monster.size(); i++) {
                    // 取出第一个坦克
                    Monster mon = GamePanel.monster.get(i);
                    // 如果不是自己
                    if (mon != this) {
                        // 如果敌人的方向是向下或者向上
                        if (mon.direct == Direction.UP || mon.direct == Direction.DOWN) {
                            // 上点
                            if (this.x + size >= mon.x
                                    && this.x + size <= mon.x + size
                                    && this.y >= mon.y && this.y <= mon.y + size) {
                                return true;
                            }
                            // 下点
                            if (this.x + size >= mon.x
                                    && this.x + size <= mon.x + size
                                    && this.y + size >= mon.y
                                    && this.y + size <= mon.y + size) {
                                return true;
                            }
                        }
                        if (mon.direct == Direction.LEFT || mon.direct == Direction.RIGHT) {
                            if (this.x + size >= mon.x
                                    && this.x + size <= mon.x + size
                                    && this.y >= mon.y && this.y <= mon.y + size) {
                                return true;
                            }
                            if (this.x + size >= mon.x
                                    && this.x + size <= mon.x + size
                                    && this.y + size >= mon.y
                                    && this.y + size <= mon.y + size) {
                                return true;
                            }
                        }
                    }
                    if (type == 1) {
                        // 如果敌人的方向是向下或者向上
                        if (GamePanel.myTank.direct == Direction.UP || GamePanel.myTank.direct == Direction.DOWN) {
                            // 上点
                            if (this.x + size >= GamePanel.myTank.x
                                    && this.x + size <= GamePanel.myTank.x + size
                                    && this.y >= GamePanel.myTank.y
                                    && this.y <= GamePanel.myTank.y + size) {
                                return true;
                            }
                            // 下点
                            if (this.x + size >= GamePanel.myTank.x
                                    && this.x + size <= GamePanel.myTank.x + size
                                    && this.y + size >= GamePanel.myTank.y
                                    && this.y + size <= GamePanel.myTank.y + size) {
                                return true;
                            }
                        }
                        if (GamePanel.myTank.direct == Direction.LEFT || GamePanel.myTank.direct == Direction.RIGHT) {
                            if (this.x + size >= GamePanel.myTank.x
                                    && this.x + size <= GamePanel.myTank.x + size
                                    && this.y >= GamePanel.myTank.y
                                    && this.y <= GamePanel.myTank.y + size) {
                                return true;
                            }
                            if (this.x + size >= GamePanel.myTank.x
                                    && this.x + size <= GamePanel.myTank.x + size
                                    && this.y + size >= GamePanel.myTank.y
                                    && this.y + size <= GamePanel.myTank.y + size) {
                                return true;
                            }
                        }
                    }
                }
                break;
            case DOWN:
                // 取出所有的敌人坦克
                for (int i = 0; i < GamePanel.monster.size(); i++) {
                    // 取出第一个坦克
                    Monster mon = GamePanel.monster.get(i);
                    // 如果不是自己
                    if (mon != this) {
                        // 如果敌人的方向是向下或者向上
                        if (mon.direct == Direction.UP || mon.direct == Direction.DOWN) {
                            // 我的左点
                            if (this.x >= mon.x && this.x <= mon.x + size
                                    && this.y + size >= mon.y
                                    && this.y + size <= mon.y + size) {
                                return true;
                            }
                            // 我的右点
                            if (this.x + size >= mon.x
                                    && this.x + size <= mon.x + size
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

                            if (this.x + size >= mon.x
                                    && this.x + size <= mon.x + size
                                    && this.y + size >= mon.y
                                    && this.y + size <= mon.y + size) {
                                return true;
                            }
                        }
                    }
                    if (type == 1) {
                        // 如果敌人的方向是向下或者向上
                        if (GamePanel.myTank.direct == Direction.UP || GamePanel.myTank.direct == Direction.DOWN) {
                            // 我的左点
                            if (this.x >= GamePanel.myTank.x
                                    && this.x <= GamePanel.myTank.x + size
                                    && this.y + size >= GamePanel.myTank.y
                                    && this.y + size <= GamePanel.myTank.y + size) {
                                return true;
                            }
                            // 我的右点
                            if (this.x + size >= GamePanel.myTank.x
                                    && this.x + size <= GamePanel.myTank.x + size
                                    && this.y + size >= GamePanel.myTank.y
                                    && this.y + size <= GamePanel.myTank.y + size) {
                                return true;
                            }
                        }
                        if (GamePanel.myTank.direct == Direction.LEFT || GamePanel.myTank.direct == Direction.RIGHT) {
                            if (this.x >= GamePanel.myTank.x
                                    && this.x <= GamePanel.myTank.x + size
                                    && this.y + size >= GamePanel.myTank.y
                                    && this.y + size <= GamePanel.myTank.y + size) {
                                return true;
                            }

                            if (this.x + size >= GamePanel.myTank.x
                                    && this.x + size <= GamePanel.myTank.x + size
                                    && this.y + size >= GamePanel.myTank.y
                                    && this.y + size <= GamePanel.myTank.y + size) {
                                return true;
                            }
                        }
                    }
                }
                break;
            case LEFT:
                // 取出所有的敌人坦克
                for (int i = 0; i < GamePanel.monster.size(); i++) {
                    // 取出第一个坦克
                    Monster mon = GamePanel.monster.get(i);
                    // 如果不是自己
                    if (mon != this) {
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
                        if (mon.direct == Direction.LEFT || mon.direct == Direction.RIGHT) {
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
                    if (type == 1) {
                        // 如果敌人的方向是向下或者向上
                        if (GamePanel.myTank.direct == Direction.UP || GamePanel.myTank.direct == Direction.DOWN) {
                            // 我的上一点
                            if (this.x >= GamePanel.myTank.x
                                    && this.x <= GamePanel.myTank.x + size
                                    && this.y >= GamePanel.myTank.y
                                    && this.y <= GamePanel.myTank.y + size) {
                                return true;
                            }
                            // 下一点
                            if (this.x >= GamePanel.myTank.x
                                    && this.x <= GamePanel.myTank.x + size
                                    && this.y + size >= GamePanel.myTank.y
                                    && this.y + size <= GamePanel.myTank.y + size) {
                                return true;
                            }
                        }
                        if (GamePanel.myTank.direct == Direction.LEFT || GamePanel.myTank.direct == Direction.RIGHT) {
                            // 上一点
                            if (this.x >= GamePanel.myTank.x
                                    && this.x <= GamePanel.myTank.x + size
                                    && this.y >= GamePanel.myTank.y
                                    && this.y <= GamePanel.myTank.y + size) {
                                return true;
                            }
                            if (this.x >= GamePanel.myTank.x
                                    && this.x <= GamePanel.myTank.x + size
                                    && this.y + size >= GamePanel.myTank.y
                                    && this.y + size <= GamePanel.myTank.y + size) {
                                return true;
                            }
                        }
                    }
                }
                break;
        }
        return false;
    }

    // 禁止几秒
    private void StopSeconds() {
        Direction oldDirect = this.direct;
        int waitingTime = r.nextInt(6);
        try {
            Thread.sleep(waitingTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        direct = oldDirect;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(CommonUtil.SLEEPTIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
            switch (this.direct) {
                case UP:
                    // 说明坦克正在向上移动
                    for (int i = 0; i < movesteps; i++) {
                        // 判断坦克是否遇到障碍物，遇到就停止
                        if (!isTouchOtherTank(direct) && !isTouchBWall()
                                && !isTouchCWall() && !isTouchBorder()) {
                            this.move();
                            // 判断坦克行走后是否遇到障碍物，遇到就停止并还原移动
                            if (isTouchOtherTank(direct) || isTouchBWall()
                                    || isTouchCWall() || isTouchBorder()) {
                                this.chang2OldDirect();
                                StopSeconds();
                            }
                        } else {
                            break;
                        }
                        try {
                            Thread.sleep(WaitingTime);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case RIGHT:
                    for (int i = 0; i < movesteps; i++) {
                        // 判断坦克是否遇到障碍物，遇到就停止
                        if (!isTouchOtherTank(direct) && !isTouchBWall()
                                && !isTouchCWall() && !isTouchBorder()) {
                            this.move();
                            // 判断坦克行走后是否遇到障碍物，遇到就停止并还原移动
                            if (isTouchOtherTank(direct) || isTouchBWall()
                                    || isTouchCWall() || isTouchBorder()) {
                                this.chang2OldDirect();
                                StopSeconds();
                            }
                        } else {
                            break;
                        }
                        try {
                            Thread.sleep(WaitingTime);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case DOWN:
                    // 向下
                    for (int i = 0; i < movesteps; i++) {
                        // 判断坦克是否遇到障碍物，遇到就停止
                        if (!isTouchOtherTank(direct) && !isTouchBWall()
                                && !isTouchCWall() && !isTouchBorder()) {
                            this.move();
                            // 判断坦克行走后是否遇到障碍物，遇到就停止并还原移动
                            if (isTouchOtherTank(direct) || isTouchBWall()
                                    || isTouchCWall() || isTouchBorder()) {
                                this.chang2OldDirect();
                                StopSeconds();
                            }
                        } else {
                            break;
                        }
                        try {
                            Thread.sleep(WaitingTime);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case LEFT:
                    // 向左
                    for (int i = 0; i < movesteps; i++) {
                        // 判断坦克是否遇到障碍物，遇到就停止
                        if (!isTouchOtherTank(direct) && !isTouchBWall()
                                && !isTouchCWall() && !isTouchBorder()) {
                            this.move();
                            // 判断坦克行走后是否遇到障碍物，遇到就停止并还原移动
                            if (isTouchOtherTank(direct) || isTouchBWall()
                                    || isTouchCWall() || isTouchBorder()) {
                                this.chang2OldDirect();
                                StopSeconds();
                            }
                        } else {
                            break;
                        }
                        try {
                            Thread.sleep(WaitingTime);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
            // 添加子弹
            AddBullets();

            try {
                // 让坦克随机产生一个随机的新的方向
                Thread.sleep(WaitingForRandom);
                this.direct = getRandomDirection();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 判断敌人坦克是否死亡
            if (!this.isLive) {
                // 让坦克死亡后退出线程
                break;
            }
        }
    }

    private void AddBullets() {
        // 判断是否需要给坦克加入新的子弹
        if (isLive) {
            if (bullets.size() < bulletOneTime) {
                if (GamePanel.myTank.isLive && bullets.size() > 2) {
                    // 启动声音
                    new Audio("Shot.wav").start();
                }
                // 没有子弹，添加
                Bullet bullet = null;
                switch (direct) {
                    case UP:
                        bullet = new Bullet(x + (size - 8) / 2, y, Direction.UP);
                        break;
                    case RIGHT:
                        bullet = new Bullet(x + size / 2, y + (size - 6) / 2, Direction.RIGHT);
                        break;
                    case DOWN:
                        bullet = new Bullet(x + (size - 10) / 2, y + size / 2, Direction.DOWN);
                        break;
                    case LEFT:// 左
                        bullet = new Bullet(x, y + (size - 6) / 2, Direction.LEFT);
                        break;
                }
                this.bullets.add(bullet);
                // 启动子弹线程
                CommonUtil.getInstance().startSingleThread(bullet);
            }
        }
    }

    public boolean isTouchCWall() {
        for (int i = 0; i < GamePanel.CWalls.size(); i++) {
            CommonWall CWall = GamePanel.CWalls.get(i);
            for (int j = 0; j < GamePanel.monster.size(); j++) {
                Monster mon = GamePanel.monster.get(j);
                if (CWall.getRect().intersects(mon.getRect())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isTouchBWall() {
        for (int i = 0; i < GamePanel.BWalls_1.size(); i++) {
            BlockWall BWall = GamePanel.BWalls_1.get(i);
            for (int j = 0; j < GamePanel.monster.size(); j++) {
                Monster mon = GamePanel.monster.get(j);
                if (BWall.getRect().intersects(mon.getRect())) {
                    return true;
                }
            }
        }
        return false;
    }

}