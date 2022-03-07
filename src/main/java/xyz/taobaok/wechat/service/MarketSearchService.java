package xyz.taobaok.wechat.service;

import xyz.taobaok.wechat.bean.dataoke.SearchWord;
import xyz.taobaok.wechat.bean.dataoke.ShopImteList;
import xyz.taobaok.wechat.bean.dataoke.TaobaoItem;
import xyz.taobaok.wechat.toolutil.R;

import java.util.List;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2022/2/11   17:51
 * @Version 1.0
 */
public interface MarketSearchService {
    R<List<String>> getHotword();

    R<List<ShopImteList>> searchWord(SearchWord imteIitle);

    R<TaobaoItem> getItem(String itemId);
}
