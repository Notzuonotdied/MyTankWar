import UIElement.CommonWall;
import Util.CommonUtil;
import Util.Direction;

import java.io.*;

public class Recorder {

    private static FileWriter fw = null;
    private static BufferedWriter bw = null;
    private static FileReader fr = null;
    private static BufferedReader br = null;
    private String fileName = "myRecorder.txt";

    public String getFileName() {
        return fileName;
    }

    public void SaveCWallInfo() {
        try {
            // 创建
            fw = new FileWriter(fileName, true);
            bw = new BufferedWriter(fw);

            // 保存所有活着的怪物的坐标和方向
            for (int i = 0; i < CommonWall.getInstance().getCWallsSize(); i++) {
                if (CommonWall.getInstance().isLive) {
                    // 把活的怪物的数据读取出来
                    // 把活的怪物的数据写入到文本中
                    // 2作为标识符，作为普通墙的标识符
                    bw.write("2" + " " + CommonWall.getInstance().getCWallAt(i).getX()
                            + " " + CommonWall.getInstance().getCWallAt(i).getY() + "\r\n");
                }
            }
        } catch (Exception e) {
            // 将错误输出
            e.printStackTrace();
        } finally {
            // 关闭流，先开后关，后开先关
            try {
                bw.close();
                fw.close();
            } catch (IOException e) {
                // 捕获错误并输出
                e.printStackTrace();
            }
        }
    }

    public void SaveMyTankInfo() {
        try {
            // 创建
            fw = new FileWriter(fileName, true);
            bw = new BufferedWriter(fw);

            // 保存我的坦克的坐标和方向
            if (GamePanel.myTank.isLive) {
                backupData(GamePanel.myTank);
            }
        } catch (Exception e) {
            // 将错误输出
            e.printStackTrace();
        } finally {
            // 关闭流，先开后关，后开先关
            try {
                bw.close();
                fw.close();
            } catch (IOException e) {
                // 捕获错误并输出
                e.printStackTrace();
            }
        }
    }

    public void SaveMonsterInfo() {
        try {
            // 创建
            fw = new FileWriter(fileName);
            bw = new BufferedWriter(fw);
            // 保存所有活着的怪物的坐标和方向
            for (int i = 0; i < Monster.getInstance().getMonsterSize(); i++) {
                Monster mon = Monster.getInstance().getMonsterAt(i);
                if (mon.isLive) {
                    backupData(mon);
                }
            }
        } catch (Exception e) {
            // 将错误输出
            e.printStackTrace();
        } finally {
            // 关闭流，先开后关，后开先关
            try {
                bw.close();
                fw.close();
            } catch (IOException e) {
                // 捕获错误并输出
                e.printStackTrace();
            }
        }
    }

    private void backupData(TankMember tankMember) {
        StringBuilder sb = new StringBuilder();
        // 把我的坦克的数据读取出来
        sb.append(tankMember.x).append(" ").append(tankMember.y).append(" ")
                .append(tankMember.direct).append(" ");
        for (int i = 0; i < tankMember.bullets.size(); i++) {
            Bullet bullet = tankMember.bullets.get(i);
            if (bullet.isLive) {
                // 如果子弹的存在状态是真的话，就保存子弹的数据累加到字符串中
                sb.append(bullet.x).append(" ").append(bullet.y).append(" ").append(bullet.direct).append(" ");
            }
        }

        // 把坦克的数据写入到文本中
        // 0作为标识符，表示我的坦克的数据
        // 1作为标识符，表示怪物的数据
        try {
            bw.write("0" + " " + sb.toString() + "\r\n");
        } catch (Exception e) {
            // 将错误输出
            e.printStackTrace();
        }
    }

    /**
     * 从记录文本文件中恢复数据
     * 原理：将数据从文本中一行行读取出来后对游戏面板中的怪物集合和我的坦克进行初始化
     * 也就是说省略了实例化创建我的坦克和怪物坦克的过程。
     */
    public void ReInfo() {
        try {
            fr = new FileReader(fileName);
            br = new BufferedReader(fr);
            String Info;

            Bullet bullet;

            // 临时变量
            int tempX;
            int tempY;
            int tempD;

            while ((Info = br.readLine()) != null) {

                String[] RecoveryInfo = Info.split(" ");
                // 类型转换，作为判断标识符
                int n = Integer.parseInt(RecoveryInfo[0]);

                // 如果第一个标识符是1，数据就是怪物的
                if (n == 1) {
                    // 类型转换
                    tempX = Integer.parseInt(RecoveryInfo[1]);
                    tempY = Integer.parseInt(RecoveryInfo[2]);
                    tempD = Integer.parseInt(RecoveryInfo[3]);

                    // 恢复并进行初始化
                    Monster mon = new Monster(tempX, tempY);
                    mon.direct = getDirection(tempD);
                    mon.isLive = true;
                    Monster.getInstance().add2Monsters(mon);
                    // 启动敌人坦克
                    Thread mont = new Thread(mon);
                    mont.start();

                    // 子弹的数据
                    int num = (RecoveryInfo.length - 1) / 3 - 1;
                    if (RecoveryInfo.length > 4) {
                        for (int i = 0; i < num; i++) {
                            if (i < num) {
                                // 类型转换
                                tempX = Integer
                                        .parseInt(RecoveryInfo[3 * i + 4]);
                                tempY = Integer
                                        .parseInt(RecoveryInfo[3 * i + 5]);
                                tempD = Integer
                                        .parseInt(RecoveryInfo[3 * i + 6]);

                                // 恢复并进行初始化
                                bullet = new Bullet(tempX, tempY, getDirection(tempD));
                                mon.bullets.add(bullet);

                                // 启动子弹线程
                                CommonUtil.getInstance().startCachedThread(bullet);
                            }
                        }
                    }
                }

                // 如果第一个标识符是0，数据就是我的坦克的
                if (n == 0) {
                    // 类型转换
                    tempX = Integer.parseInt(RecoveryInfo[1]);
                    tempY = Integer.parseInt(RecoveryInfo[2]);
                    tempD = Integer.parseInt(RecoveryInfo[3]);

                    // 恢复并进行初始化
                    GamePanel.myTank = new MyTank(tempX, tempY);
                    GamePanel.myTank.direct = getDirection(tempD);
                    GamePanel.myTank.isLive = true;

                    // 子弹的数据
                    int num = (RecoveryInfo.length - 1) / 3 - 1;
                    if (RecoveryInfo.length > 4) {
                        for (int i = 0; i < num; i++) {
                            if (i < num) {
                                // 类型转换
                                tempX = Integer
                                        .parseInt(RecoveryInfo[3 * i + 4]);
                                tempY = Integer
                                        .parseInt(RecoveryInfo[3 * i + 5]);
                                tempD = Integer
                                        .parseInt(RecoveryInfo[3 * i + 6]);

                                // 恢复并进行初始化
                                bullet = new Bullet(tempX, tempY, getDirection(tempD));
                                GamePanel.myTank.bullets.add(bullet);
                                CommonUtil.getInstance().startCachedThread(bullet);
                            }
                        }
                    }
                }

                // 如果第一个标识符是2，数据就是普通墙的
                if (n == 2) {
                    // 类型转换
                    tempX = Integer.parseInt(RecoveryInfo[1]);
                    tempY = Integer.parseInt(RecoveryInfo[2]);

                    // 恢复并进行初始化
                    CommonWall.getInstance().isLive = true;
                    CommonWall.getInstance().add2CWalls(new CommonWall(tempX, tempY));
                }
            }
        } catch (Exception e) {
            // 捕获错误信息
            e.printStackTrace();
        } finally {
            try {
                br.close();
                fr.close();
            } catch (Exception e2) {
                // 捕获错误
                e2.printStackTrace();
            }
        }
    }

    private Direction getDirection(int directNum) {
        switch (directNum) {
            case 0:
                return Direction.UP;
            case 1:
                return Direction.RIGHT;
            case 2:
                return Direction.DOWN;
            case 3:
                return Direction.LEFT;
            default:
                return Direction.UP;
        }
    }

}