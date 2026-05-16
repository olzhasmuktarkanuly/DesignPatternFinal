package com.mygdx.game.inventory;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Weapon;
import com.mygdx.game.factories.WeaponFactory;
import com.mygdx.game.items.PickupType;

public class Inventory {

    private final WeaponFactory weaponFactory = new WeaponFactory();
    private HandSlot selectedSlot = HandSlot.NONE;

    private Weapon primaryWeapon = null;
    private Weapon pistolWeapon = null;
    private Weapon currentWeapon = null;

    private int primaryAmmo = 0;
    private int pistolAmmo = 0;

    private String meleeWeaponName = "None";
    private boolean hasMedkit = false;

    private String boostItemName = "None";
    private String throwableName = "None";
    private int throwableCount = 0;

    public void reset() {
        selectedSlot = HandSlot.NONE;

        primaryWeapon = null;
        pistolWeapon = null;
        currentWeapon = null;

        primaryAmmo = 0;
        pistolAmmo = 0;

        meleeWeaponName = "None";
        hasMedkit = false;

        boostItemName = "None";
        throwableName = "None";
        throwableCount = 0;
    }

    public HandSlot getSelectedSlot() {
        return selectedSlot;
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public int getCurrentAmmo() {
        if (currentWeapon == primaryWeapon) {
            return primaryAmmo;
        }

        if (currentWeapon == pistolWeapon) {
            return pistolAmmo;
        }

        return 0;
    }

    public void spendCurrentAmmo(int amount) {
        if (currentWeapon == primaryWeapon) {
            primaryAmmo -= amount;

            if (primaryAmmo < 0) {
                primaryAmmo = 0;
            }
        } else if (currentWeapon == pistolWeapon) {
            pistolAmmo -= amount;

            if (pistolAmmo < 0) {
                pistolAmmo = 0;
            }
        }
    }

    public boolean hasMedkit() {
        return hasMedkit;
    }

    public void consumeMedkit() {
        hasMedkit = false;
        selectedSlot = HandSlot.NONE;
        applySelectedSlot();
    }

    public String getBoostItemName() {
        return boostItemName;
    }

    public void consumeBoost() {
        boostItemName = "None";
        selectedSlot = HandSlot.NONE;
        applySelectedSlot();
    }

    public String getThrowableName() {
        return throwableName;
    }

    public int getThrowableCount() {
        return throwableCount;
    }

    public void consumeThrowable() {
        throwableCount--;

        if (throwableCount <= 0) {
            throwableCount = 0;
            throwableName = "None";
            selectedSlot = HandSlot.NONE;
        }

        applySelectedSlot();
    }

    public String getMeleeWeaponName() {
        return meleeWeaponName;
    }

    public int getPrimaryAmmo() {
        return primaryAmmo;
    }

    public int getPistolAmmo() {
        return pistolAmmo;
    }

    public String getPrimaryWeaponName() {
        if (primaryWeapon == null) {
            return "None";
        }

        return primaryWeapon.getName();
    }

    public String getPistolWeaponName() {
        if (pistolWeapon == null) {
            return "None";
        }

        return pistolWeapon.getName();
    }

    public String getSelectedItemName() {
        switch (selectedSlot) {
            case PRIMARY:
                return getPrimaryWeaponName();
            case PISTOL:
                return getPistolWeaponName();
            case MELEE:
                return meleeWeaponName;
            case THROWABLE:
                return throwableName;
            case MEDKIT:
                return hasMedkit ? "Medkit" : "None";
            case BOOST:
                return boostItemName;
            case NONE:
            default:
                return "None";
        }
    }

    public void selectNextSlot() {
        Array<HandSlot> slots = getAvailableSlots();

        if (slots.size == 0) {
            selectedSlot = HandSlot.NONE;
            currentWeapon = null;
            return;
        }

        int index = slots.indexOf(selectedSlot, true);

        if (index == -1 || index == slots.size - 1) {
            selectedSlot = slots.first();
        } else {
            selectedSlot = slots.get(index + 1);
        }

        applySelectedSlot();
    }

    public void selectPreviousSlot() {
        Array<HandSlot> slots = getAvailableSlots();

        if (slots.size == 0) {
            selectedSlot = HandSlot.NONE;
            currentWeapon = null;
            return;
        }

        int index = slots.indexOf(selectedSlot, true);

        if (index <= 0) {
            selectedSlot = slots.get(slots.size - 1);
        } else {
            selectedSlot = slots.get(index - 1);
        }

        applySelectedSlot();
    }

    public Array<HandSlot> getAvailableSlots() {
        Array<HandSlot> slots = new Array<>();

        if (primaryWeapon != null) {
            slots.add(HandSlot.PRIMARY);
        }

        if (pistolWeapon != null) {
            slots.add(HandSlot.PISTOL);
        }

        if (!meleeWeaponName.equals("None")) {
            slots.add(HandSlot.MELEE);
        }

        if (!throwableName.equals("None") && throwableCount > 0) {
            slots.add(HandSlot.THROWABLE);
        }

        if (hasMedkit) {
            slots.add(HandSlot.MEDKIT);
        }

        if (!boostItemName.equals("None")) {
            slots.add(HandSlot.BOOST);
        }

        return slots;
    }

    public void applySelectedSlot() {
        if (selectedSlot == HandSlot.PRIMARY) {
            currentWeapon = primaryWeapon;
        } else if (selectedSlot == HandSlot.PISTOL) {
            currentWeapon = pistolWeapon;
        } else {
            currentWeapon = null;
        }
    }

    public PickupType getPrimaryPickupType() {
        if (primaryWeapon == null) {
            return null;
        }

        if (primaryWeapon.getName().equals("AK47")) {
            return PickupType.PRIMARY_AK47;
        }

        if (primaryWeapon.getName().equals("Sniper")) {
            return PickupType.PRIMARY_SNIPER;
        }

        return null;
    }

    public PickupType getMeleePickupType() {
        if (meleeWeaponName.equals("Bat")) {
            return PickupType.BAT;
        }

        if (meleeWeaponName.equals("Katana")) {
            return PickupType.KATANA;
        }

        if (meleeWeaponName.equals("Shovel")) {
            return PickupType.SHOVEL;
        }

        return null;
    }

    public PickupType getBoostPickupType() {
        if (boostItemName.equals("Pills")) {
            return PickupType.PILLS;
        }

        if (boostItemName.equals("Adrenaline")) {
            return PickupType.ADRENALINE;
        }

        return null;
    }

    public PickupType getThrowablePickupType() {
        if (throwableName.equals("Bomb")) {
            return PickupType.BOMB;
        }

        if (throwableName.equals("Molotov")) {
            return PickupType.MOLOTOV;
        }

        return null;
    }

    public boolean pickUp(PickupType type) {
        switch (type) {
            case PRIMARY_AK47:
                if (getPrimaryPickupType() == PickupType.PRIMARY_AK47) {
                    return false;
                }

                primaryWeapon = weaponFactory.createWeapon(type);
                selectedSlot = HandSlot.PRIMARY;
                applySelectedSlot();
                return true;

            case PRIMARY_SNIPER:
                if (getPrimaryPickupType() == PickupType.PRIMARY_SNIPER) {
                    return false;
                }

                primaryWeapon = weaponFactory.createWeapon(type);
                selectedSlot = HandSlot.PRIMARY;
                applySelectedSlot();
                return true;

            case PISTOL:
                if (pistolWeapon != null) {
                    return false;
                }

                pistolWeapon = weaponFactory.createWeapon(type);
                selectedSlot = HandSlot.PISTOL;
                applySelectedSlot();
                return true;

            case PRIMARY_AMMO:
                primaryAmmo += 30;
                return true;

            case PISTOL_AMMO:
                pistolAmmo += 20;
                return true;

            case BAT:
                if (getMeleePickupType() == PickupType.BAT) {
                    return false;
                }

                meleeWeaponName = "Bat";
                selectedSlot = HandSlot.MELEE;
                applySelectedSlot();
                return true;

            case KATANA:
                if (getMeleePickupType() == PickupType.KATANA) {
                    return false;
                }

                meleeWeaponName = "Katana";
                selectedSlot = HandSlot.MELEE;
                applySelectedSlot();
                return true;

            case SHOVEL:
                if (getMeleePickupType() == PickupType.SHOVEL) {
                    return false;
                }

                meleeWeaponName = "Shovel";
                selectedSlot = HandSlot.MELEE;
                applySelectedSlot();
                return true;

            case MEDKIT:
                if (hasMedkit) {
                    return false;
                }

                hasMedkit = true;
                selectedSlot = HandSlot.MEDKIT;
                applySelectedSlot();
                return true;

            case PILLS:
                if (getBoostPickupType() == PickupType.PILLS) {
                    return false;
                }

                boostItemName = "Pills";
                selectedSlot = HandSlot.BOOST;
                applySelectedSlot();
                return true;

            case ADRENALINE:
                if (getBoostPickupType() == PickupType.ADRENALINE) {
                    return false;
                }

                boostItemName = "Adrenaline";
                selectedSlot = HandSlot.BOOST;
                applySelectedSlot();
                return true;

            case BOMB:
                if (getThrowablePickupType() == PickupType.BOMB) {
                    return false;
                }

                throwableName = "Bomb";
                throwableCount = 1;
                selectedSlot = HandSlot.THROWABLE;
                applySelectedSlot();
                return true;

            case MOLOTOV:
                if (getThrowablePickupType() == PickupType.MOLOTOV) {
                    return false;
                }

                throwableName = "Molotov";
                throwableCount = 1;
                selectedSlot = HandSlot.THROWABLE;
                applySelectedSlot();
                return true;

            default:
                return false;
        }
    }
}
