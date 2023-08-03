package net.swordie.ms.client.jobs;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.jobs.adventurer.*;
import net.swordie.ms.client.jobs.adventurer.archer.*;
import net.swordie.ms.client.jobs.adventurer.magician.*;
import net.swordie.ms.client.jobs.adventurer.pirate.*;
import net.swordie.ms.client.jobs.adventurer.thief.*;
import net.swordie.ms.client.jobs.adventurer.warrior.*;
import net.swordie.ms.client.jobs.cygnus.*;
import net.swordie.ms.client.jobs.legend.*;
import net.swordie.ms.client.jobs.nova.*;
import net.swordie.ms.client.jobs.resistance.*;
import net.swordie.ms.client.jobs.resistance.demon.Demon;
import net.swordie.ms.client.jobs.resistance.demon.DemonAvenger;
import net.swordie.ms.client.jobs.resistance.demon.DemonSlayer;
import net.swordie.ms.client.jobs.sengoku.*;

import java.lang.reflect.InvocationTargetException;

/**
 * Created on 12/14/2017.
 */
public class JobManager {
    private static final Class<Job>[] jobClasses = new Class[] {
            Beginner.class,
            Warrior.class,
            Hero.class,
            Paladin.class,
            DarkKnight.class,
            Magician.class,
            FirePoison.class,
            IceLightning.class,
            Bishop.class,
            Archer.class,
            Bowmaster.class,
            Marksman.class,
            Thief.class,
            NightLord.class,
            Shadower.class,
            DualBlade.class,
            Pirate.class,
            Buccaneer.class,
            Corsair.class,
            Cannoneer.class,
            Jett.class,

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

            Demon.class,
            DemonSlayer.class,
            DemonAvenger.class,
            Citizen.class,
            BattleMage.class,
            Blaster.class,
            Mechanic.class,
            WildHunter.class,
            Xenon.class,

            AngelicBuster.class,
            Kaiser.class,

            Hayato.class,
            Kanna.class,

            BeastTamer.class,
            Kinesis.class,
            Zero.class,
            PinkBean.class,
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
