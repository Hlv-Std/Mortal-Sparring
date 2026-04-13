package model;

public class AttackHitbox {
    public double x, y;
    public Rect hitbox;
    public double damage;
    private int duration;

    public AttackHitbox(double x, double y, Rect hitbox, double damage, int duration){
        this.x = x;
        this.y = y;
        this.hitbox = hitbox;
        this.damage = damage;
        this.duration = duration;
    }

    public boolean isAlive(){ return duration > 0; }
    public void decrease() { duration -= 1; }
}
