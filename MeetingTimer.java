import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
public class MeetingTimer implements ActionListener {// 会议计时器类，继承自JFrame，并实现了ActionListener接口
    private JLabel totalTimeLabel, remainingTimeLabel, usedTimeLabel, currentTimeLabel;
    private JTextField totalTimeField, remainingTimeField, usedTimeField, currentTimeField;
    private JButton startButton, pauseButton, resetButton, toggleButton;
    private Timer timer;
    private int totalTime, remainingTime, usedTime;
    private boolean isMinutes = false; // 默认时间单位为秒
    private JLabel unitLabel;
    private JLabel lblNewLabel;
    private int cont = 0;//判断是否再次点击暂停

    public MeetingTimer() {
        JFrame frame = new JFrame();
        frame.setTitle("");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(491, 337);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        try {
            BufferedImage iconImage = ImageIO.read(new File("C:\\Users\\Lenovo\\Desktop\\icon.png"));
            frame.setIconImage(iconImage);
        } catch (Exception ignored) {

        }

        // 初始化界面组件
        totalTimeLabel = new JLabel("总时间:");
        totalTimeLabel.setFont(new Font("黑体", Font.PLAIN, 20));
        totalTimeLabel.setBounds(122, 105, 100, 30);
        totalTimeField = new JTextField("0", 5);
        totalTimeField.setFont(new Font("宋体", Font.PLAIN, 20));
        totalTimeField.setBounds(247, 105, 100, 30);
        totalTimeField.setEditable(true);

        remainingTimeLabel = new JLabel("剩余时间:");
        remainingTimeLabel.setFont(new Font("黑体", Font.PLAIN, 20));
        remainingTimeLabel.setBounds(122, 197, 90, 30);
        remainingTimeField = new JTextField("0", 5);
        remainingTimeField.setFont(new Font("宋体", Font.PLAIN, 20));
        remainingTimeField.setBounds(247, 197, 100, 30);
        remainingTimeField.setEditable(false);

        usedTimeLabel = new JLabel("已用时间:");
        usedTimeLabel.setFont(new Font("黑体", Font.PLAIN, 20));
        usedTimeLabel.setBounds(122, 157, 100, 30);
        usedTimeField = new JTextField("0", 5);
        usedTimeField.setFont(new Font("宋体", Font.PLAIN, 20));
        usedTimeField.setBounds(247, 157, 100, 30);
        usedTimeField.setEditable(false);

        currentTimeLabel = new JLabel("当前时间:"); // 新增当前时间标签
        currentTimeLabel.setFont(new Font("黑体", Font.PLAIN, 20));
        currentTimeLabel.setBounds(122, 20, 100, 30);
        currentTimeField = new JTextField("", 10); // 新增当前时间文本框
        currentTimeField.setFont(new Font("宋体", Font.PLAIN, 20));
        currentTimeField.setBounds(247, 20, 100, 30);
        currentTimeField.setEditable(false); // 设置当前时间文本框为不可编辑

        startButton = new JButton("开始");
        startButton.setBounds(0, 260, 100, 30);
        startButton.addActionListener(this);

        pauseButton = new JButton("暂停");
        pauseButton.setBounds(122, 260, 100, 30);
        pauseButton.addActionListener(this);
        pauseButton.setEnabled(false);

        resetButton = new JButton("重置");
        resetButton.setBounds(247, 260, 100, 30);
        resetButton.addActionListener(this);
        resetButton.setEnabled(false);

        toggleButton = new JButton("切换到分钟");
        toggleButton.setBounds(376, 260, 100, 30);
        toggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isMinutes = !isMinutes;
                if (isMinutes) {
                    toggleButton.setText("切换到秒");
                } else {
                    toggleButton.setText("切换到分钟");
                }
            }
        });
        frame.getContentPane().setLayout(null);

        frame.getContentPane().add(totalTimeLabel);
        frame.getContentPane().add(remainingTimeLabel);
        frame.getContentPane().add(usedTimeLabel);
        frame.getContentPane().add(currentTimeLabel);
        frame.getContentPane().add(totalTimeField);
        frame.getContentPane().add(remainingTimeField);
        frame.getContentPane().add(usedTimeField);
        frame.getContentPane().add(currentTimeField);
        frame.getContentPane().add(startButton);
        frame.getContentPane().add(pauseButton);
        frame.getContentPane().add(resetButton);
        frame.getContentPane().add(toggleButton);

        lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon("C:\\Users\\Lenovo\\Desktop\\meetingtime.jpg"));
        lblNewLabel.setBounds(0, 0, 476, 307);
        frame.getContentPane().add(lblNewLabel);
        frame.setVisible(true);

        // 启动定时器，每秒更新当前时间
        Timer currentTimeTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                currentTimeField.setText(sdf.format(new Date()));
            }
        });
        currentTimeTimer.start();
    }

    public void actionPerformed(ActionEvent e) { // 处理按钮点击事件
        if (e.getSource() == startButton&&cont==0) {//点击开始
            int inputTime = Integer.parseInt(totalTimeField.getText());//
            totalTime = isMinutes ? inputTime * 60 : inputTime;
            remainingTime = totalTime;
            remainingTimeField.setText(formatTime(remainingTime));
            usedTimeField.setText("0");
            timer = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    remainingTime--;
                    remainingTimeField.setText(formatTime(remainingTime));
                    usedTimeField.setText(formatTime(totalTime - remainingTime));
                    if (remainingTime == 0) {
                        timer.stop();
                        JOptionPane.showMessageDialog(null, "会议时间到!");
                        startButton.setEnabled(false);
                        pauseButton.setEnabled(false);
                        resetButton.setEnabled(true);
                    }
                }
            });
            timer.start();
            startButton.setEnabled(false);
            pauseButton.setEnabled(true);
            resetButton.setEnabled(true);
        }else if (e.getSource() == startButton&&cont==1) {//点击开始
            int inputTime = Integer.parseInt(remainingTimeField.getText());//
            int total = Integer.parseInt(totalTimeField.getText());
            totalTime = isMinutes ? total * 60 : total;
            remainingTime = isMinutes ? inputTime * 60 : inputTime;
            remainingTimeField.setText(formatTime(remainingTime));
            timer = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    remainingTime--;
                    remainingTimeField.setText(formatTime(remainingTime));
                    usedTimeField.setText(formatTime(totalTime - remainingTime));
                    if (remainingTime == 0) {
                        timer.stop();
                        JOptionPane.showMessageDialog(null, "会议时间到!");
                        startButton.setEnabled(false);
                        pauseButton.setEnabled(false);
                        resetButton.setEnabled(true);
                    }
                }
            });
            timer.start();
            startButton.setEnabled(false);
            pauseButton.setEnabled(true);
            resetButton.setEnabled(true);
        } else if (e.getSource() == pauseButton) {//点击暂停
            timer.stop();
            cont=1;
            startButton.setEnabled(true);
            pauseButton.setEnabled(false);
            resetButton.setEnabled(true);
        } else if (e.getSource() == resetButton) {//点击重置
            timer.stop();
            totalTimeField.setText("0");
            remainingTimeField.setText("0");
            usedTimeField.setText("0");
            startButton.setEnabled(true);
            pauseButton.setEnabled(false);
            resetButton.setEnabled(false);
        }
    }

    private String formatTime(int timeInSeconds) { // 格式化时间为分钟和秒
        if (isMinutes) {
            int minutes = timeInSeconds / 60;
            int seconds = timeInSeconds % 60;
            return String.format("%02d:%02d", minutes, seconds);
        } else {
            return String.valueOf(timeInSeconds);
        }
    }

    public static void main(String[] args) {
        new MeetingTimer();
    }
}