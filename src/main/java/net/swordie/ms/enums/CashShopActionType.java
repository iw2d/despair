package net.swordie.ms.enums;

import net.swordie.ms.util.Util;

import java.util.Arrays;

/**
 * @author Sjonnie
 * Created on 7/7/2018.
 */
public enum CashShopActionType {
    ShowCategory(11),
    ShowSearchResult(13),
    AddFavorite(14),
    Like(15),
    RemoveFavorite(16),
    ShowFavorites(18),
    Res_19(19),
    Res_20(20),
    Res_21(21),
    Res_22(22),
    Req_ShowCategory(101),
    Req_Leave(102),
    Req_Favorite(103),
    Req_UnFavorite(104),
    Req_Like(105),
    Req_UnLike(106),
    Req_ShowSearchResult(107),
    Req_ShowFavorites(109),
    Req_ClickSpecial(112),
    Req_BuyCart(113)
    ;

    private int val;

    CashShopActionType(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public static CashShopActionType getByVal(int val) {
        return Util.findWithPred(Arrays.asList(values()), csat -> csat.getVal() == val);
    }
}
