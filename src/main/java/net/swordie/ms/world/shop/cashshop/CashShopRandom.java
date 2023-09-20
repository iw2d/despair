package net.swordie.ms.world.shop.cashshop;

import javax.persistence.*;

@Entity
@Table(name = "cs_random")
public class CashShopRandom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int reward;
    private int gender;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int option) {
        this.reward = reward;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
