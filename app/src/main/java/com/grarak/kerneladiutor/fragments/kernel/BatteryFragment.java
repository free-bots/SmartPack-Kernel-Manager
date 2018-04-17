/*
 * Copyright (C) 2015-2016 Willi Ye <williye97@gmail.com>
 *
 * This file is part of Kernel Adiutor.
 *
 * Kernel Adiutor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Kernel Adiutor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Kernel Adiutor.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.grarak.kerneladiutor.fragments.kernel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.grarak.kerneladiutor.R;
import com.grarak.kerneladiutor.fragments.ApplyOnBootFragment;
import com.grarak.kerneladiutor.fragments.DescriptionFragment;
import com.grarak.kerneladiutor.fragments.RecyclerViewFragment;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.kernel.battery.Battery;
import com.grarak.kerneladiutor.views.recyclerview.CardView;
import com.grarak.kerneladiutor.views.recyclerview.RecyclerViewItem;
import com.grarak.kerneladiutor.views.recyclerview.SeekBarView;
import com.grarak.kerneladiutor.views.recyclerview.SelectView;
import com.grarak.kerneladiutor.views.recyclerview.StatsView;
import com.grarak.kerneladiutor.views.recyclerview.SwitchView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willi on 26.06.16.
 */
public class BatteryFragment extends RecyclerViewFragment {

    private Battery mBattery;

    private StatsView mLevel;
    private StatsView mVoltage;
    private StatsView mChargingStatus;

    private int mBatteryLevel;
    private int mBatteryVoltage;

    @Override
    protected void init() {
        super.init();

        mBattery = Battery.getInstance(getActivity());
    }

    @Override
    protected void addItems(List<RecyclerViewItem> items) {
        levelInit(items);
        voltageInit(items);
        chargeRateInit(items);
        mChargingStatus = new StatsView();
        if (Battery.haschargingstatus()) {
        items.add(mChargingStatus);
        }
        if (mBattery.hasBlx()) {
            blxInit(items);
        }
        if (mBattery.hasForceFastCharge()) {
            fastChargeInit(items);
        }
    }

    @Override
    protected void postInit() {
        super.postInit();

        if (itemsSize() > 2) {
            addViewPagerFragment(ApplyOnBootFragment.newInstance(this));
        }
        addViewPagerFragment(DescriptionFragment.newInstance(getString(R.string.capacity),
                mBattery.getCapacity() + getString(R.string.mah)));
    }

    private void levelInit(List<RecyclerViewItem> items) {
        mLevel = new StatsView();
        mLevel.setTitle(getString(R.string.level));

        items.add(mLevel);
    }

    private void voltageInit(List<RecyclerViewItem> items) {
        mVoltage = new StatsView();
        mVoltage.setTitle(getString(R.string.voltage));

        items.add(mVoltage);
    }

    private void fastChargeInit(List<RecyclerViewItem> items) {
        CardView fastChargeCard = new CardView(getActivity());
        fastChargeCard.setTitle(getString(R.string.acci));

        if (mBattery.hasForceFastCharge()) {
            SelectView forceFastCharge = new SelectView();
            forceFastCharge.setTitle(getString(R.string.fast_charge));
            forceFastCharge.setSummary(getString(R.string.fast_charge_summary));
            forceFastCharge.setItems(mBattery.enableForceFastCharge(getActivity()));
            forceFastCharge.setItem(mBattery.getForceFastCharge());
            forceFastCharge.setOnItemSelected(new SelectView.OnItemSelected() {
            @Override
            public void onItemSelected(SelectView selectView, int position, String item) {
                mBattery.setForceFastCharge(position, getActivity());
            }
        });

            fastChargeCard.addItem(forceFastCharge);

    }
        
        if (mBattery.hasFastChargeControlAC()) {
            SelectView ACLevelCard = new SelectView();
            ACLevelCard.setTitle(getString(R.string.charge_level_ac));
            ACLevelCard.setSummary(getString(R.string.charge_level_ac_summary));
            ACLevelCard.setItems(mBattery.getFastChargeControlAC());
            ACLevelCard.setItem(mBattery.getFastChargeCustomAC());
            ACLevelCard.setOnItemSelected(new SelectView.OnItemSelected() {
            @Override
            public void onItemSelected(SelectView selectView, int position, String item) {
                mBattery.setFastChargeControlAC(item, getActivity());
            }
        });
            fastChargeCard.addItem(ACLevelCard);

    }
            
        if (mBattery.hasFastChargeControlUSB()) {
            SelectView USBLevelCard = new SelectView();
            USBLevelCard.setTitle(getString(R.string.charge_level_usb));
            USBLevelCard.setSummary(getString(R.string.charge_level_usb_summary));
            USBLevelCard.setItems(mBattery.getFastChargeControlUSB());
            USBLevelCard.setItem(mBattery.getFastChargeCustomUSB());
            USBLevelCard.setOnItemSelected(new SelectView.OnItemSelected() {
            @Override
            public void onItemSelected(SelectView selectView, int position, String item) {
                mBattery.setFastChargeControlUSB(item, getActivity());
            }
        });
            fastChargeCard.addItem(USBLevelCard);
    }
    
        if (mBattery.hasFastChargeControlWIRELESS()) {
            SelectView WirelessLevelCard = new SelectView();
            WirelessLevelCard.setTitle(getString(R.string.charge_level_wireless));
            WirelessLevelCard.setSummary(getString(R.string.charge_level_wireless_summary));
            WirelessLevelCard.setItems(mBattery.getFastChargeControlWIRELESS());
            WirelessLevelCard.setItem(mBattery.getFastChargeCustomWIRELESS());
            WirelessLevelCard.setOnItemSelected(new SelectView.OnItemSelected() {
            @Override
            public void onItemSelected(SelectView selectView, int position, String item) {
                mBattery.setFastChargeControlWIRELESS(item, getActivity());
           }
        });
            fastChargeCard.addItem(WirelessLevelCard);
    }
    
        if (mBattery.hasMtpForceFastCharge()) {
        SwitchView MtpFastCharge = new SwitchView();
        MtpFastCharge.setTitle(getString(R.string.mtp_fast_charge));
        MtpFastCharge.setSummary(getString(R.string.mtp_fast_charge_summary));
        MtpFastCharge.setChecked(mBattery.isMtpForceFastChargeEnabled());
        MtpFastCharge.addOnSwitchListener(new SwitchView.OnSwitchListener() {
            @Override
            public void onChanged(SwitchView switchView, boolean isChecked) {
                mBattery.enableMtpForceFastCharge(isChecked, getActivity());
            }
        });

            fastChargeCard.addItem(MtpFastCharge);
    }
    
        if (mBattery.hasScreenCurrentLimit()) {
        SwitchView ScreenLimit = new SwitchView();
        ScreenLimit.setTitle(getString(R.string.screen_limit));
        ScreenLimit.setSummary(getString(R.string.screen_limit_summary));
        ScreenLimit.setChecked(mBattery.isScreenCurrentLimit());
        ScreenLimit.addOnSwitchListener(new SwitchView.OnSwitchListener() {
            @Override
            public void onChanged(SwitchView switchView, boolean isChecked) {
                mBattery.enableScreenCurrentLimit(isChecked, getActivity());
            }
        });

            fastChargeCard.addItem(ScreenLimit);
        }

        if (fastChargeCard.size() > 0) {
            items.add(fastChargeCard);
        }
    }

    private void blxInit(List<RecyclerViewItem> items) {
        List<String> list = new ArrayList<>();
        list.add(getString(R.string.disabled));
        for (int i = 0; i <= 100; i++) {
            list.add(String.valueOf(i));
        }

        SeekBarView blx = new SeekBarView();
        blx.setTitle(getString(R.string.blx));
        blx.setSummary(getString(R.string.blx_summary));
        blx.setItems(list);
        blx.setProgress(mBattery.getBlx());
        blx.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
            @Override
            public void onStop(SeekBarView seekBarView, int position, String value) {
                mBattery.setBlx(position, getActivity());
            }

            @Override
            public void onMove(SeekBarView seekBarView, int position, String value) {
            }
        });

        items.add(blx);
    }

    private void chargeRateInit(List<RecyclerViewItem> items) {
        CardView chargeRateCard = new CardView(getActivity());
        chargeRateCard.setTitle(getString(R.string.charge_rate));

        if (mBattery.hasChargeRateEnable()) {
            SwitchView chargeRate = new SwitchView();
            chargeRate.setSummary(getString(R.string.charge_rate));
            chargeRate.setChecked(mBattery.isChargeRateEnabled());
            chargeRate.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    mBattery.enableChargeRate(isChecked, getActivity());
                }
            });

            chargeRateCard.addItem(chargeRate);
        }

        if (mBattery.hasChargingCurrent()) {
            SeekBarView chargingCurrent = new SeekBarView();
            chargingCurrent.setTitle(getString(R.string.charging_current));
            chargingCurrent.setSummary(getString(R.string.charging_current_summary));
            chargingCurrent.setUnit(getString(R.string.ma));
            chargingCurrent.setMax(1500);
            chargingCurrent.setMin(100);
            chargingCurrent.setOffset(10);
            chargingCurrent.setProgress(mBattery.getChargingCurrent() / 10 - 10);
            chargingCurrent.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    mBattery.setChargingCurrent((position + 10) * 10, getActivity());
                }

                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }
            });

            chargeRateCard.addItem(chargingCurrent);
        }

        if (chargeRateCard.size() > 0) {
            items.add(chargeRateCard);
        }
    }

    private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mBatteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            mBatteryVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
        }
    };

    @Override
    protected void refresh() {
        super.refresh();
        if (mLevel != null) {
            mLevel.setStat(mBatteryLevel + "%");
        }
        if (mVoltage != null) {
            mVoltage.setStat(mBatteryVoltage + " mV");
        }
        if (mChargingStatus != null) {
			if (Battery.getchargingstatus() >= 10000) {
			float dc = Battery.getchargingstatus() /1000;
			if (Battery.isChargeStatus()){
			mChargingStatus.setTitle("Disconnected");
            mChargingStatus.setStat(0 + (" mA"));}
            else{
			mChargingStatus.setTitle("Charging");
            mChargingStatus.setStat(String.valueOf(dc) + (" mA"));}}
			else {
			float cd = Battery.getchargingstatus();
			if (Battery.isChargeStatus()){
			mChargingStatus.setTitle("Disconnected");
            mChargingStatus.setStat(0 + (" mA"));}
            else{
			mChargingStatus.setTitle("Charging");
            mChargingStatus.setStat(String.valueOf(cd) + (" mA"));}}
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            getActivity().unregisterReceiver(mBatteryReceiver);
        } catch (IllegalArgumentException ignored) {
        }
    }

}
