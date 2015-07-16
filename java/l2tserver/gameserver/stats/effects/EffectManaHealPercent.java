/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package l2tserver.gameserver.stats.effects;

import l2tserver.gameserver.model.L2Effect;
import l2tserver.gameserver.model.actor.instance.L2DoorInstance;
import l2tserver.gameserver.network.serverpackets.StatusUpdate;
import l2tserver.gameserver.stats.Env;
import l2tserver.gameserver.templates.skills.L2AbnormalType;
import l2tserver.gameserver.templates.skills.L2EffectTemplate;

public class EffectManaHealPercent extends L2Effect
{
	public EffectManaHealPercent(Env env, L2EffectTemplate template)
	{
		super(env, template);
	}
	
	// Special constructor to steal this effect
	public EffectManaHealPercent(Env env, L2Effect effect)
	{
		super(env, effect);
	}

	@Override
	public L2AbnormalType getAbnormalType()
	{
		return L2AbnormalType.HEAL_OVER_TIME;
	}
	
	/**
	 * 
	 * @see l2tserver.gameserver.model.L2Abnormal#onStart()
	 */
	@Override
	public boolean onStart()
	{
		if (getEffected().isDead())
			return false;
		
		if (getEffected() instanceof L2DoorInstance)
			return false;
		
		double mp = getEffected().getCurrentMp();
		double maxmp = getEffected().getMaxMp();
		mp += calc() * maxmp / 100.0;
		if (mp > maxmp)
			mp = maxmp;
		
		getEffected().setCurrentMp(mp);
		StatusUpdate sump = new StatusUpdate(getEffected());
		sump.addAttribute(StatusUpdate.CUR_MP, (int) mp);
		getEffected().sendPacket(sump);
		
		return true;
	}
	
	/**
	 * 
	 * @see l2tserver.gameserver.model.L2Abnormal#onActionTime()
	 */
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}