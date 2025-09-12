package com.ufpa.physics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;


import javax.imageio.ImageIO;
import javax.swing.event.MouseInputAdapter;

public class Simulation extends Component{
    class MagnetCreationMouseAdapter extends MouseInputAdapter{
        private Component c;
        //private boolean movingPos = false;
        //private boolean movingNeg = false;
        public MagnetCreationMouseAdapter(Component c){
            this.c = c;
        }
        @Override
        public void mousePressed(MouseEvent e){
            int button = e.getButton();
    
            if(button == MouseEvent.BUTTON1){
                int x = e.getX();
                int y = e.getY();
                //if (magnet != null) {
                //    if(
                //        Math.hypot(
                //            x - magnet.getPositive().getX(), 
                //            y- magnet.getPositive().getY())
                //        < 100
                //    ){
                //        movingPos = true;
                //        System.out.println("movingPos");
                //    }
                //    if(
                //        Math.hypot(
                //            x - magnet.getNegative().getX(), 
                //            y - magnet.getNegative().getY())
                //        < 100
                //    ){
                //        movingNeg= true;
                //        System.out.println("movingNeg");
                //        return;
                //    }
                //}else{
                positiveOfNewMag = new Point2D.Float(x, y);
                //}
                System.out.println("Pressed button 1: Creating a new magnet.");
            }
        }
        @Override
        public void mouseDragged(MouseEvent e){
            System.out.println(1);
            int x = e.getX();
            int y = e.getY();
            //if (magnet != null && e.getButton() == MouseEvent.BUTTON1 ) {
            //    if(movingPos){
            //        Point2D pos = magnet.getPositive();
            //        pos.setLocation(new Point2D.Double(x,y));
            //        System.out.println(pos);
            //    }
            //    if(movingNeg){
            //        Point2D neg= magnet.getNegative();
            //        neg.setLocation(new Point2D.Double(x,y));
            //        System.out.println(neg);
            //    }
            //    c.repaint();
            //}
        }
    
        @Override 
        public void mouseReleased(MouseEvent e){
            //movingPos = false;
            //movingNeg = false;
            int button = e.getButton();
            if(button == MouseEvent.BUTTON1){
                int x = e.getX();
                int y = e.getY();
                //if(movingPos){
                //    movingPos = false;
                //    c.repaint();return;
                //}
                //if(movingNeg){
                //    movingNeg = false;
                //    c.repaint();return;
                //}
                negativeOfNewMag = new Point2D.Float(x, y);
                System.out.println("Released button1: Created the magnet.");
                Magnet magnet2 = new Magnet(positiveOfNewMag, negativeOfNewMag);
                magnet = magnet2;
                c.repaint();
            }
        }
    
        @Override
        public void mouseClicked(MouseEvent e){
            int button = e.getButton();
            if(button == MouseEvent.BUTTON1){
                System.out.println("Clicked button1");
    
            }
            if(button == MouseEvent.BUTTON2){
                System.out.println("Clicked button2");
            }
            if(button == MouseEvent.BUTTON3){
                System.out.println("Clicked button3");
            }
        }
    }
    private BufferedImage image;
    private Magnet magnet;
    private Point2D positiveOfNewMag;
    private Point2D negativeOfNewMag;
    private Stroke magnetStroke = new BasicStroke(40);
    private BufferedImage compass;
    private MouseListener mouseListener = new MagnetCreationMouseAdapter(this);

    public Simulation(){
        try{
            compass = ImageIO.read(getClass().getClassLoader().getResource("compass.png"));
        }catch(IOException exception){
            System.exit(1);
        }

        image = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
        for(int y = 0; y < 800; y++){
            for(int x = 0; x < 800; x++){
                image.setRGB(x, y, 0x010510);
            }
        }

        this.addMouseListener(mouseListener);
    }
    
    private void drawCompasses(Graphics2D g) throws Exception{
        for(int y = 0; y < 800; y+=16 + 16){
            for(int x = 0; x < 800; x+=16+16){
                double angle = getAngle(x, y);
                AffineTransform rotation = AffineTransform.getRotateInstance(angle, compass.getWidth()/2, compass.getHeight()/2);
                AffineTransformOp rotationOp = new AffineTransformOp(rotation, AffineTransformOp.TYPE_BILINEAR);
                BufferedImage rotated = new BufferedImage(compass.getWidth(), compass.getHeight(), compass.getType());
                g.drawImage(rotationOp.filter(compass, rotated), x, y, null);
            }
        }
    }

    private double getAngle(double x, double y){
        Magnet m = magnet;
        Point2D positive = m.getPositive();
        Point2D negative = m.getNegative();

        double anglePositive = Math.atan2(y - positive.getY(), x - positive.getX());
        double angleNegative = Math.atan2(y - negative.getY(), x - negative.getX());
        double angleCrazy = Math.atan2(positive.getX() - negative.getX(), positive.getY() - negative.getY());
        //return 2*Math.PI/2 + (anglePositive + angleNegative)/2;
        return -(2*Math.PI - angleCrazy) + (anglePositive + angleNegative);

    }

    @Override
    public void paint(Graphics g){
        if(magnet == null) return;
        Graphics2D graphics = (Graphics2D) g;
        //Stroke defStroke = graphics.getStroke();
        graphics.drawImage(image, 0, 0, null);
        try{drawCompasses(graphics);} catch(Exception e){}
        Point2D positive = magnet.getPositive();
        Point2D negative = magnet.getNegative();
        graphics.setColor(Color.MAGENTA);
        graphics.fillOval((int)positive.getX()-40, (int)positive.getY()-40, 80, 80);
        graphics.setColor(Color.CYAN);
        graphics.fillOval((int)negative.getX()-40, (int)negative.getY()-40, 80, 80);
        graphics.setStroke(magnetStroke);
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.drawLine((int)positive.getX(), (int)positive.getY(), (int)negative.getX(), (int)negative.getY());
    }
}
