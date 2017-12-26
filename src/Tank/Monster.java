package Tank;

import UIElement.BlockWall;
import UIElement.CommonWall;
import Util.Audio;
import Util.CommonUtil;
import Util.Direction;

import java.awt.*;
import java.util.Random;
import java.util.Vector;

import static Util.CommonUtil.size;


/**
 * 怪物类
 * 由于怪物是多个的，且要求互不影响，所以要做成多线程的
 */
public class Monster extends TankMember implements Runnable {
    // 定义怪物的停顿时间
    private static final int WaitingTime = 23;
    // 定义让坦克随机产生一个随机的新的方向的等待时间
    private static final int WaitingForRandom = 30;
    // 定义一个敌人的坦克集合
    private static final Vector<Monster> monsters = new Vector<>();
    // 定义怪物连发子弹的数量
    public static int bulletOneTime = 2;
    // 定义怪物的数量
    public static int monsterSize = 6;
    // 定义坦克的生命值
    public static int blood = 50;
    // 定义随机变量
    private static Random r = new Random();
    private static Monster monster;

    private Monster() {
        super();
    }

    Monster(int x, int y) {
        super(x, y);
    }

    public static Monster getInstance() {
        if (monster == null) {
            synchronized (Monster.class) {
                if (monster == null) {
                    monster = new Monster();
                }
            }
        }
        return monster;
    }

    public int getMonsterSize() {
        return monsters.size();
    }

    public void clearMonsters() {
        monsters.clear();
    }

    public Monster getMonsterAt(int index) {
        return monsters.get(index);
    }

    public void add2Monsters(Monster monster) {
        monsters.add(monster);
    }

    private int getRandomMoveStep() {
        return r.nextInt(23) + 23;
    }

    /**
     * 恢复怪物坦克
     */
    public void restoreMonsters() {
        for (int i = 0; i < monsterSize; i++) {
            // 定义坦克的位置
            Monster mon = new Monster((i + 1) * 80, getRandomMoveStep());
            mon.setDirect(Direction.DOWN);
            // 启动敌人坦克
            CommonUtil.getInstance().startCachedThread(mon);
            // 将敌人坦克添加到坦克集合中
            monsters.add(mon);
            // 为敌人添加子弹
            Bullet bullet = new Bullet(mon.getX() + size / 2, mon.getY() + size / 2, Direction.DOWN);
            // 加到子弹集合中
            mon.bullets.add(bullet);
            // 启动子弹线程
            CommonUtil.getInstance().startCachedThread(bullet);
        }
    }

    /**
     * 绘制所有的Monster
     *
     * @param g     画笔
     * @param score 分数
     */
    public int drawAllMonster(Graphics g, int score) {
        for (int i = 0; i < monsters.size(); i++) {
            Monster mon = monsters.get(i);
            if (mon.isLive) {
                mon.drawTank(mon.getX(), mon.getY(), g, 1);
                for (int j = 0; j < mon.bullets.size(); j++) {
                    // 取出一个子弹
                    Bullet bullet = mon.bullets.get(j);
                    if (bullet.isBulletReady(mon)) {
                        // 画出子弹的轨迹
                        bullet.drawBullet(g);
                        // 判断是否击中了我的坦克
                        bullet.HitMyTank();
                    }
                    if (!bullet.isLive) {
                        mon.bullets.remove(bullet);
                    }
                }
            }
            // 怪物死亡就移除
            if (!mon.isLive) {
                monsters.remove(mon);
                ++score;
            }
        }
        return score;
    }

    /**
     * 定义我的坦克的移动函数
     */
    public void move() {
        // 保存旧的位置,保证出现错误的移动可以恢复到原先的位置
        set2OldDirect();
        // 选择移动方向
        switch (direct) {
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

    /**
     * 防止重叠
     */
    @Override
    public boolean isTouchOtherTank() {
        synchronized (monsters) {
            for (Monster mon : monsters) {
                if (this != mon) {
                    if (mon.getRect().intersects(this.getRect())) {
                        return true;
                    }
                    if (mon.getRect().intersects(MyTank.getInstance().getRect())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * 禁止几秒
     */
    private void StopSeconds() {
        Direction oldDirect = this.direct;
        sleepForMoment(r.nextInt(12) + 1);
        direct = oldDirect;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(CommonUtil.SLEEPTIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < getRandomMoveStep(); i++) {
                // 判断坦克是否遇到障碍物，遇到就停止
                if (canMove()) {
                    this.move();
                    // 判断坦克行走后是否遇到障碍物，遇到就停止并还原移动
                    if (afterMoveIsOkay()) {
                        StopSeconds();
                    }
                } else {
                    break;
                }
                sleepForMoment(WaitingTime);
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
                monsters.remove(this);
                break;
            }
        }
    }

    private void AddBullets() {
        // 判断是否需要给坦克加入新的子弹
        if (isLive) {
            if (bullets.size() < bulletOneTime) {
                if (MyTank.getInstance().isLive && bullets.size() > bulletOneTime) {
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

    @Override
    public boolean isTouchCWall() {
        for (int i = 0; i < CommonWall.getInstance().getCWallsSize(); i++) {
            synchronized (monsters) {
                for (Monster mon : monsters) {
                    if (CommonWall.getInstance().getCWallRectAt(i).intersects(mon.getRect())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isTouchBWall() {
        for (int i = 0; i < BlockWall.getInstance().getBWalls_1Size(); i++) {
            synchronized (monsters) {
                for (Monster mon : monsters) {
                    if (BlockWall.getInstance().getBWallRectAt(i).intersects(mon.getRect())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}