package com.github.tacowasa059.generatetextintheworld.commands;

import com.github.tacowasa059.generatetextintheworld.GenerateTextInTheWorld;
import org.bukkit.ChatColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class GenerateCommand implements CommandExecutor {
    private final GenerateTextInTheWorld plugin;
    private static List<Material> woolMaterial = Arrays.asList(
            Material.RED_WOOL,
            Material.YELLOW_WOOL,
            Material.BLUE_WOOL,
            Material.GREEN_WOOL,
            Material.WHITE_WOOL,
            Material.BLACK_WOOL,
            Material.LIGHT_BLUE_WOOL,
            Material.GRAY_WOOL,
            Material.LIGHT_GRAY_WOOL,
            Material.BROWN_WOOL
    );
    public GenerateCommand(GenerateTextInTheWorld plugin){
        this.plugin=plugin;
    }
    // /GenerateText distance <text>

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player p=(Player)sender;
            if(args.length<2){//引数不足判定
                p.sendMessage(ChatColor.RED +"引数が不足しています");
                p.sendMessage(ChatColor.GREEN+"(Ex)"+ChatColor.RED +"/GenerateText distance <text>");
            }
            else{
                //プレイヤーの座標+direction*distanceで計算
                double distance=Double.parseDouble(args[0]);
                Location location=p.getLocation().add(p.getLocation().getDirection().multiply(distance));
                //textの左上の座標がlocation
                //表示するtextを取得
                StringBuilder builder=new StringBuilder();
                for(int i=1;i<args.length;i++){
                    builder.append(args[i]);
                    if(i<args.length-1) builder.append(" ");
                }
                String text= builder.toString();
                text=text.replace("\"", "");
                int size=plugin.getConfig().getInt("size");
                //座標が不適切であるとき
                if(location.getY()<(double)size||location.getY()>=255) p.sendMessage(ChatColor.RED+"y座標の位置が不適切です。文字全体が0~255に収まるように視点と距離を調整して下さい。");
                //blockを置き換える
                else PlaceBlock(text,location);
            }
        }
        else{
            System.out.println("このコマンドはプレイヤーからしか実行できません。");
        }
        return true;
    }

    //textをもとにblockを置き換える関数
    private void PlaceBlock(String text,Location location){
        int size=plugin.getConfig().getInt("size");
        //色の並び替え
        List<Integer> list1= Arrays.asList(IntStream.rangeClosed(0, woolMaterial.size()-1).boxed().toArray(Integer[]::new));
        Collections.shuffle(list1);
        if(Math.abs(location.getDirection().getZ())>Math.abs(location.getDirection().getX())) {
            //中心位置に揃える
            location.add(Math.signum(location.getDirection().getZ()) * (size - 5) * text.length() / 2.0, size / 2.0, 0.0);
            //1文字ずつ処理する
            for (int k = 0; k < text.length(); k++) {
                BufferedImage img = new BufferedImage(size - 5, size, BufferedImage.TYPE_INT_ARGB);
                Graphics g = img.getGraphics();
                g.setColor(Color.BLACK);
                Font font1 = new Font("ＭＳＰゴシック", Font.PLAIN, (int) (size * 0.75));
                g.setFont(font1);
                g.drawString(String.valueOf(text.charAt(k)), 0, size - 5);
                //System.out.println(text.charAt(k));
                Location fire_loc = location.clone();
                fire_loc.add(-Math.signum(location.getDirection().getZ()) * (double) (img.getWidth() / 2 + (size - 5) * k), -(double) (img.getHeight() / 2), 0.0);
                Firework firework = fire_loc.getWorld().spawn(fire_loc, Firework.class);
                FireworkMeta data = (FireworkMeta) firework.getFireworkMeta();
                data.addEffects(FireworkEffect.builder().withColor(org.bukkit.Color.RED).withColor(org.bukkit.Color.BLUE).withColor(org.bukkit.Color.YELLOW).with(FireworkEffect.Type.BALL_LARGE).withFlicker().build());
                data.setPower(1);
                firework.setFireworkMeta(data);


                for (int i = 0; i < img.getHeight(); i++) {
                    for (int j = 0; j < img.getWidth(); j++) {
                        int pixel = img.getRGB(j, i);
                        int a = pixel >> 24;//Aの数値(0~255)
                        Location loc = location.clone();
                        loc.add(-Math.signum(location.getDirection().getZ()) * (double) (j + (size - 5) * k), -(double) i, 0.0);
                        if (a != 0) {
                            loc.getBlock().setType(woolMaterial.get(list1.get(k % woolMaterial.size())));
                        } else {
                            loc.getBlock().setType(Material.AIR);
                        }
                    }
                }
            }
        }
        else{
            //中心位置に揃える
            location.add(0.0, size / 2.0, -Math.signum(location.getDirection().getX()) * (size - 5) * text.length() / 2.0);
            //1文字ずつ処理する
            for (int k = 0; k < text.length(); k++) {
                BufferedImage img = new BufferedImage(size - 5, size, BufferedImage.TYPE_INT_ARGB);
                Graphics g = img.getGraphics();
                g.setColor(Color.BLACK);
                Font font1 = new Font("ＭＳＰゴシック", Font.PLAIN, (int) (size * 0.75));
                g.setFont(font1);
                g.drawString(String.valueOf(text.charAt(k)), 0, size - 5);
                //System.out.println(text.charAt(k));
                Location fire_loc = location.clone();
                fire_loc.add(0.0, -(double) (img.getHeight() / 2), Math.signum(location.getDirection().getX()) * (double) (img.getWidth() / 2 + (size - 5) * k));
                Firework firework = fire_loc.getWorld().spawn(fire_loc, Firework.class);
                FireworkMeta data = (FireworkMeta) firework.getFireworkMeta();
                data.addEffects(FireworkEffect.builder().withColor(org.bukkit.Color.RED).withColor(org.bukkit.Color.BLUE).withColor(org.bukkit.Color.YELLOW).with(FireworkEffect.Type.BALL_LARGE).withFlicker().build());
                data.setPower(1);
                firework.setFireworkMeta(data);


                for (int i = 0; i < img.getHeight(); i++) {
                    for (int j = 0; j < img.getWidth(); j++) {
                        int pixel = img.getRGB(j, i);
                        int a = pixel >> 24;//Aの数値(0~255)
                        Location loc = location.clone();
                        loc.add(0.0, -(double) i, Math.signum(location.getDirection().getX()) * (double) (j + (size - 5) * k));
                        if (a != 0) {
                            loc.getBlock().setType(woolMaterial.get(list1.get(k % woolMaterial.size())));
                        } else {
                            loc.getBlock().setType(Material.AIR);
                        }
                    }
                }
            }
        }
    }
}
