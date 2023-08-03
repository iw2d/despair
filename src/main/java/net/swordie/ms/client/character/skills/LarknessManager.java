package net.swordie.ms.client.character.skills;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.jobs.legend.Luminous;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.connection.packet.UserLocal;

/**
 * Created on 2/10/2018.
 */
public class LarknessManager {
	private Char chr;
	private int darkGauge;
	private int lightGauge;
	private int darkFeathers;
	private int lightFeathers;

	private boolean dark;

	public LarknessManager(Char chr) {
		this.chr = chr;

	}

	public int getDarkGauge() {
		return darkGauge;
	}

	public void setDarkGauge(int darkGauge) {
		this.darkGauge = darkGauge;
	}

	public int getLightGauge() {
		return lightGauge;
	}

	public void setLightGauge(int lightGauge) {
		this.lightGauge = lightGauge;
	}

	public int getDarkFeathers() {
		return darkFeathers;
	}

	public void setDarkFeathers(int darkFeathers) {
		this.darkFeathers = darkFeathers;
	}

	public int getLightFeathers() {
		return lightFeathers;
	}

	public void setLightFeathers(int lightFeathers) {
		this.lightFeathers = lightFeathers;
	}

	public boolean isDark() {
		return dark;
	}

	public void setDark(boolean dark) {
		this.dark = dark;
	}

	public boolean isEquilibrium() {
		TemporaryStatManager tsm = chr.getTemporaryStatManager();
		int larknessSkillId = tsm.getOption(CharacterTemporaryStat.Larkness).rOption;
		return larknessSkillId == Luminous.EQUILIBRIUM_LIGHT;
	}

	/**
	 * Adds a dark feather, up to a maximum of 5.
	 */
	private void addDarkFeather() {
		if (getDarkFeathers() < 5) {
			setDarkFeathers(getDarkFeathers() + 1);
		}
	}

	/**
	 * Adds a light feather, up to a maximum of 5.
	 */
	private void addLightFeather() {
		if (getLightFeathers() < 5) {
			setLightFeathers(getLightFeathers() + 1);
		}
	}

	/**
	 * Adds a specified amount to the given gauge. Note: Max value of a gauge is 10000.
	 *
	 * @param amount
	 * 		The amount to add to the gauge
	 * @param dark
	 * 		Which gauge to add the amount to
	 */
	public void addGauge(int amount, boolean dark) {
		if (dark) {
			int newGauge = getDarkGauge() + amount;
			if (newGauge >= 10000) {
				newGauge -= 10000;
				addDarkFeather();
			}
			setDarkGauge(newGauge);
		} else {
			int newGauge = getLightGauge() + amount;
			if (newGauge >= 10000) {
				newGauge -= 10000;
				addLightFeather();
			}
			setLightGauge(newGauge);
		}
		updateInfo();
	}

	/**
	 * Changes to specified mode. Includes decrementing feathers, and updating
	 * the client.
	 *
	 * @param dark mode to change to
	 */
	public void changeMode(boolean dark) {
		if (dark) {
			int feathers = getDarkFeathers();
			if (feathers > 0) {
				setDarkFeathers(feathers - 1);
			}
		} else {
			int feathers = getLightFeathers();
			if (feathers > 0) {
				setLightFeathers(feathers - 1);
			}
		}
		setDark(dark);
		updateInfo();
	}

	public void encode(OutPacket outPacket) {
		outPacket.encodeInt(getDarkGauge());
		outPacket.encodeInt(getLightGauge());
		outPacket.encodeInt(getDarkFeathers());
		outPacket.encodeInt(getLightFeathers());
		outPacket.encodeInt(0); // unk
	}

	/**
	 * Sends a packet to update the client's larkness state
	 */
	public void updateInfo() {
		chr.write(UserLocal.incLarknessReponse(this));
	}
}
