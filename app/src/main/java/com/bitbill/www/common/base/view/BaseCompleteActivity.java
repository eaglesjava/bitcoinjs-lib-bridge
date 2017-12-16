package com.bitbill.www.common.base.view;

import android.view.Menu;
import android.view.MenuItem;

import com.bitbill.www.R;

/**
 * Created by isanwenyu@163.com on 2017/12/16.
 */
public abstract class BaseCompleteActivity extends BaseToolbarActivity {
    /**
     * 完成动作
     */
    protected abstract void completeAction();

    @Override
    protected boolean hasHomeAsUpEnabled() {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.complete_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_complete) {
            //完成
            finish();
            completeAction();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
