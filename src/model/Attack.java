package model;

public class Attack {
    private final Character who;
    public double x, y;
    public double offx, offy;
    public Rect hitbox;
    public final int damage;
    private int duration;

    public Attack(Character who, double offx, double offy, Rect hitbox, int damage, int duration){
        this.who      = who;
        this.x        = who.x;
        this.y        = who.y;
        this.offx     = offx;
        this.offy     = offy;
        this.hitbox   = hitbox;
        this.damage   = damage;
        this.duration = duration;
    }

    public void advance(){
        x = who.x + offx;
        y = who.y + offy;
        duration--;
    }
    public boolean isAlive(){ return duration > 0; }
}
