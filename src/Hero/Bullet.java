package Hero;

import UIElement.BlockWall;
import UIElement.CommonWall;
import Util.Audio;
import Util.CommonUtil;
import Util.Direction;

import java.awt.*;
import java.util.Vector;

import static Util.CommonUtil.rangX;
import static Util.CommonUtil.rangY;

public class Bullet implements Runnable {
    // 定义子弹的尺寸
    private static final int bulletWidth = 12;
    private static final int bulletHeight = 12;
    // 定义子弹的速度
    private static final int speed = 16;
    // 定义每一颗子弹的伤害值
    public static int damage = 25;
    // 子弹图片初始化
    // 定义为全局静态变量
    private static Image[] BulletImages;
    private static Bullet bullet;

    static {
        BulletImages = new Image[]{
                // 上 左 下 右
                Toolkit.getDefaultToolkit().getImage(
                        Bullet.class.getResource("/images/bulletUp.gif")),
                Toolkit.getDefaultToolkit().getImage(
                        Bullet.class.getResource("/images/bulletRight.gif")),
                Toolkit.getDefaultToolkit().getImage(
                        Bullet.class.getResource("/images/bulletDown.gif")),
                Toolkit.getDefaultToolkit().getImage(
                        Bullet.class.getResource("/images/bulletLeft.gif")),};
    }

    // 定义坐标
    int x;
    int y;
    // 定义子弹的存在状态
    boolean isLive = true;
    // 定义方向
    Direction direct;

    private Bullet() {

    }

    // 构造函数
    Bullet(int x, int y, Direction direct) {
        this.x = x;
        this.y = y;
        this.direct = direct;
    }

    public static Bullet getInstance() {
        if (bullet == null) {
            synchronized (Bullet.class) {
                if (bullet == null) {
                    bullet = new Bullet();
                }
            }
        }
        return bullet;
    }

    // 画出子弹
    public void drawBullet(Graphics g) {
        switch (direct) {
            case UP:
                g.drawImage(BulletImages[0], x, y, null);
                break;
            case RIGHT:
                g.drawImage(BulletImages[1], x, y, null);
                break;
            case DOWN:
                g.drawImage(BulletImages[2], x, y, null);
                break;
            case LEFT:
                g.drawImage(BulletImages[3], x, y, null);
                break;
        }
    }

    boolean isBulletReady(TankMember tankMember) {
        return isLive && BulletComeAcrossCWall(this) && BulletComeAcrossBWall(this, tankMember.getBlockWall());
    }

    /**
     * 判断怪物和我的子弹与普通墙相遇的时候--击中普通墙，则普通墙消失，子弹也消失
     */
    private boolean BulletComeAcrossCWall(Bullet bullet) {
        for (int i = 0; i < CommonWall.getInstance().getCWallsSize(); i++) {
            if (CommonWall.getInstance().getCWallRectAt(i).intersects(bullet.getBulletRect())) {
                bullet.isLive = false;
                CommonWall.getInstance().isLive = false;
                CommonWall.getInstance().removeCWall(i);
                return false;
            }
        }
        return true;
    }

    /**
     * 判断怪物和我的子弹与石墙相遇的时候--子弹消失
     */
    private boolean BulletComeAcrossBWall(Bullet bullet, BlockWall blockWall) {
        for (int i = 0; i < blockWall.getBWalls_1Size(); i++) {
            if (blockWall.getBWallRectAt(i).intersects(bullet.getBulletRect())) {
                bullet.isLive = false;
                return false;
            }
        }
        return true;
    }

    /**
     * 判断我的坦克的子弹和怪物的子弹相遇的情况，子弹相遇后抵消，都消失
     *
     * @return true->相遇，消失；false->没有遇到，不消失
     */
    private boolean isBulletComeAcross() {
        for (int i = 0; i < Monster.getInstance().getMonsterSize(); i++) {
            Monster mon = Monster.getInstance().getMonsterAt(i);
            for (int j = 0; j < mon.bullets.size(); j++) {
                Bullet b = mon.bullets.get(j);
                if (b.getBulletRect().intersects(getBulletRect())) {
                    // 两者的存在状态都为死亡
                    isLive = false;
                    mon.bullets.remove(b);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean ifDrawBullet(TankMember tankMember, Graphics g) {
        for (int i = 0; i < tankMember.bullets.size(); i++) {
            Bullet bullet = tankMember.bullets.get(i);
            if (bullet != null && bullet.isBulletReady(tankMember) && !bullet.isBulletComeAcross()) {
                // 画出子弹的轨迹
                bullet.drawBullet(g);
                // 判断是否集中了怪物
                bullet.HitMonster();
            }
            assert bullet != null;
            if (!bullet.isLive) {
                // 如果子弹存在状态为假就移除子弹
                return false;
            }
        }
        return true;
    }

    /**
     * 判断怪物是否击中了我
     */
    public void HitMyTank() {
        // 取出每一只怪物
        for (int i = 0; i < Monster.getInstance().getMonsterSize(); i++) {
            // 取出怪物
            Monster mon = Monster.getInstance().getMonsterAt(i);
            Vector<Bullet> bullets = mon.getBullets();
            // 取出怪物的每一个子弹
            for (Bullet bullet : bullets) {
                if (MyTank.getInstance().isLive) {
                    // 调用判断是否相交的函数
                    hitTank(bullet, MyTank.getInstance());
                }
            }
        }
    }

    /**
     * 判断我的坦克的子弹是否击中了怪物
     */
    private void HitMonster() {
        // 判断是否击中了怪物
        for (int i = 0; i < MyTank.getInstance().bullets.size(); i++) {
            // 取出子弹
            Bullet bullet = MyTank.getInstance().bullets.get(i);
            if (bullet.isLive) {
                // 取出每只怪物，进行匹配
                for (int j = 0; j < Monster.getInstance().getMonsterSize(); j++) {
                    // 取出怪物
                    Monster mon = Monster.getInstance().getMonsterAt(j);
                    if (mon.isLive) {
                        // 调用判断是否相交的函数
                        hitTank(bullet, mon);
                    }
                }
            }
        }
    }

    /**
     * 判断子弹是否击中了目标--我的坦克或者是怪物
     *
     * @param bullet 子弹
     * @param tank   击中的坦克
     */
    private void hitTank(Bullet bullet, TankMember tank) {
        if (bullet.getBulletRect().intersects(tank.getRect())) {
            // 两者的存在状态都为死亡
            bullet.isLive = false;
            tank.isLive = false;
            // 把子弹从子弹集合中去除
            tank.bullets.remove(bullet);
            // 新建爆炸效果
            Bomb.getInstance().add2Bombs(new Bomb(tank.getX(), tank.getY()));
            new Audio("Explosion.wav").start();
        }
    }

    /**
     * run函数
     */
    public void run() {
        while (isLive) {
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
            try {
                Thread.sleep(CommonUtil.SLEEPTIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 子弹何时死亡
            if (x < 0 || x > rangX || y < 0 || y > rangY) {
                this.isLive = false;
                break;
            }

        }
    }

    /**
     * 调用子弹类的Rectangle函数
     */
    private Rectangle getBulletRect() {
        return new Rectangle(x, y, bulletWidth, bulletHeight);
    }

}