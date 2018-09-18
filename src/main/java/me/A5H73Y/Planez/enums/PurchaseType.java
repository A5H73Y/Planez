package me.A5H73Y.Planez.enums;

import me.A5H73Y.Planez.Planez;

public enum PurchaseType {
    PLANE("Purchase"),
    UPGRADE("Upgrade"),
    FUEL("Refuel");

    final String purchaseKey;

    PurchaseType(String purchaseKey) {
        this.purchaseKey = purchaseKey;
    }

    public double getCost() {
        return Planez.getInstance().getConfig().getDouble("Economy.Cost." + this.purchaseKey, 0.0);
    }

    public static PurchaseType fromString(String purchaseKey) {
        for (PurchaseType type : PurchaseType.values()) {
            if (type.purchaseKey.equals(purchaseKey)) {
                return type;
            }
        }
        throw new IllegalArgumentException("PurchaseType of " + purchaseKey + " not found.");
    }
}
