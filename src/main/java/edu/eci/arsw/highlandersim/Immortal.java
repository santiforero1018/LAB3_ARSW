package edu.eci.arsw.highlandersim;

import java.util.List;
import java.util.Random;

public class Immortal extends Thread {

    private ImmortalUpdateReportCallback updateCallback = null;

    private int health;

    private int defaultDamageValue;

    private boolean pausa;

    private boolean stop;

    private final List<Immortal> immortalsPopulation;

    private final List<Immortal> immortalsDead;

    private final String name;

    private final Random r = new Random(System.currentTimeMillis());

    public Immortal(String name, List<Immortal> immortalsPopulation, int health, int defaultDamageValue,
            ImmortalUpdateReportCallback ucb, List<Immortal> immortalsDead) {
        super(name);
        this.updateCallback = ucb;
        this.name = name;
        this.immortalsPopulation = immortalsPopulation;
        this.health = health;
        this.defaultDamageValue = defaultDamageValue;
        this.pausa = false;
        this.stop = false;
        this.immortalsDead = immortalsDead;
    }

    public void run() {

        while (health > 0 && !stop) {
            if (pausa) {
                this.pasueIM();
            }

            Immortal im;

            int myIndex = immortalsPopulation.indexOf(this);

            int nextFighterIndex = r.nextInt(immortalsPopulation.size());

            // avoid self-fight
            if (nextFighterIndex == myIndex) {
                nextFighterIndex = ((nextFighterIndex + 1) % immortalsPopulation.size());
            }

            im = immortalsPopulation.get(nextFighterIndex);

            this.fight(im);

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (pausa) {
                synchronized (im) {
                    try {
                        im.wait();

                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                }
            }

        }
        
        this.pasueIM();

    }

    public void fight(Immortal i2) {

        synchronized (immortalsPopulation) {
            if (i2.getHealth() > 0) {
                i2.changeHealth(i2.getHealth() - defaultDamageValue);
                this.health += defaultDamageValue;
                updateCallback.processReport("Fight: " + this + " vs " + i2 + "\n");
            } else {
                updateCallback.processReport(this + " says:" + i2 + " is already dead!\n");
            }
        }
    }

    public synchronized void resumes() {
        pausa = false;
        notifyAll();
    }

    public void pasueIM() {
        try {
            synchronized (immortalsPopulation) {
                immortalsPopulation.wait();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void setPausa(boolean pause) {
        this.pausa = pause;
    }

    public void changeHealth(int v) {
        health = v;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public String toString() {

        return name + "[" + health + "]";
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

}
