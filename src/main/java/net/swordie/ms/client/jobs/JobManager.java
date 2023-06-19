package net.swordie.ms.client.jobs;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.jobs.adventurer.*;
import net.swordie.ms.client.jobs.adventurer.archer.Archer;
import net.swordie.ms.client.jobs.adventurer.magician.Magician;
import net.swordie.ms.client.jobs.adventurer.pirate.Pirate;
import net.swordie.ms.client.jobs.adventurer.thief.Thief;
import net.swordie.ms.client.jobs.adventurer.warrior.DarkKnight;
import net.swordie.ms.client.jobs.adventurer.warrior.Hero;
import net.swordie.ms.client.jobs.adventurer.warrior.Paladin;
import net.swordie.ms.client.jobs.adventurer.warrior.Warrior;
import net.swordie.ms.client.jobs.cygnus.*;
import net.swordie.ms.client.jobs.legend.*;
import net.swordie.ms.client.jobs.nova.AngelicBuster;
import net.swordie.ms.client.jobs.nova.Kaiser;
import net.swordie.ms.client.jobs.resistance.*;
import net.swordie.ms.client.jobs.sengoku.Hayato;
import net.swordie.ms.client.jobs.sengoku.Kanna;

import java.lang.reflect.InvocationTargetException;

/**
 * Created on 12/14/2017.
 */
public class JobManager {
    private static final Class<Job>[] jobClasses = new Class[] {
            Warrior.class,
            Hero.class,
            Paladin.class,
            DarkKnight.class,
            Archer.class,
            BeastTamer.class,
            Beginner.class,
            Kinesis.class,
            Magician.class,
            PinkBean.class,
            Pirate.class,
            Thief.class,

            BlazeWizard.class,
            DawnWarrior.class,
            Mihile.class,
            NightWalker.class,
            Noblesse.class,
            ThunderBreaker.class,
            WindArcher.class,

            Aran.class,
            Evan.class,
            Legend.class,
            Luminous.class,
            Mercedes.class,
            Phantom.class,
            Shade.class,

            AngelicBuster.class,
            Kaiser.class,

            Citizen.class,
            BattleMage.class,
            Blaster.class,
            Demon.class,
            Mechanic.class,
            WildHunter.class,
            Xenon.class,

            Hayato.class,
            Kanna.class,

            Zero.class
    };

    public static Job getJobById(short id, Char chr) {
        Job job = null;
        for(Class<Job> clazz : jobClasses) {
            try {
                job = clazz.getConstructor(Char.class).newInstance(chr);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
            if (job != null && job.isHandlerOfJob(id)) {
                return job;
            }
        }
        return job;
    }
}
