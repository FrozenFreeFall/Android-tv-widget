package com.open.androidtvwidget.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.Settings;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

/**
 * 设置默认APP(类似小米的). <br>
 * 比如有很多浏览器，你可以设置默认的. <br>
 * 如何使用，查看DEMO，http://git.oschina.net/hailongqiu/demo-test <br>
 * @author hailong.qiu 356752238@qq.com
 *
 */
public class CustomApplicationHelper {

	private Context mContext;
	private PackageManager pm;

	public CustomApplicationHelper(Context context) {
		this.mContext = context;
		if (mContext != null) {
			this.pm = context.getPackageManager();
		}
	}

	public List<ResolveInfo> getSpeAppResolveInfos(Intent intent) {
		List<ResolveInfo> resolveInfos = null;
		if (intent != null && pm != null) {
			resolveInfos = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		}
		return resolveInfos;
	}

	public List<InputMethodInfo> getAllInputMethod() {
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		List<InputMethodInfo> methodList = imm.getInputMethodList();
		return methodList;
	}

	public void setDefaultInputMethod(InputMethodInfo info) {
		// 设置默认输入法.
		String packName = info.getPackageName();
		String serviceName = info.getServiceName();
		int lastIndex = serviceName.lastIndexOf(".");
		if (lastIndex != -1) {
			String setInfo = packName + "/" + serviceName.substring(lastIndex);
			Settings.Secure.putString(mContext.getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD,
					"" + setInfo);
		}
	}

	public boolean isDefualtInputMethod(InputMethodInfo info) {
		// 获取当前默认输入法.
		String currentInputmethod = Settings.Secure.getString(mContext.getContentResolver(),
				Settings.Secure.DEFAULT_INPUT_METHOD);
		if (currentInputmethod.contains("" + info.getPackageName())) {
			return true;
		}
		return false;
	}

	/**
	 * 处理Intent.
	 */
	public Intent intentForResolveInfo(ResolveInfo dri, Intent initIntent) {
		Intent intent = new Intent(initIntent);
		intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
		ActivityInfo ai = dri.activityInfo;
		intent.setComponent(new ComponentName(ai.applicationInfo.packageName, ai.name));
		return intent;
	}

	/**
	 * 设置默认APP.
	 */
	public void setDefaultApplication(Intent initIntent, ResolveInfo dri, List<ResolveInfo> resolveInfos) {
		// 获取 Intent.
		Intent intent = intentForResolveInfo(dri, initIntent);
		//
		IntentFilter filter = new IntentFilter();
		// 初始化 action.
		if (intent.getAction() != null) {
			filter.addAction(intent.getAction());
		}
		// 初始化 CATEGORY.
		Set<String> categories = intent.getCategories();
		if (categories != null) {
			for (String cat : categories) {
				filter.addCategory(cat);
			}
		}
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		//
		Uri data = intent.getData();
		int cat = dri.match & IntentFilter.MATCH_CATEGORY_MASK;

		if (cat == IntentFilter.MATCH_CATEGORY_TYPE) {
			String mimeType = intent.resolveType(mContext);
			if (mimeType != null) {
				try {
					filter.addDataType(mimeType);
				} catch (IntentFilter.MalformedMimeTypeException e) {
					filter = null;
				}
			}
		} else if (data != null && data.getScheme() != null) { // 一般是设置了数据，比如浏览器.
			filter.addDataScheme(data.getScheme());
		}

		// 设置默认应用.
		if (filter != null && pm != null) {
			final int N = resolveInfos.size();
			ComponentName[] set = new ComponentName[N];
			int bestMatch = 0;
			for (int i = 0; i < N; i++) {
				ResolveInfo r = resolveInfos.get(i);
				set[i] = new ComponentName(r.activityInfo.packageName, r.activityInfo.name);
				if (r.match > bestMatch)
					bestMatch = r.match;
			}
			pm.addPreferredActivity(filter, bestMatch, set, intent.getComponent());
		}
	}

	/**
	 * 清除默认选择. 清除之前的选项.
	 */
	public void clearDefaultApp(List<ResolveInfo> resolveInfos) {
		if (resolveInfos != null) {
			for (int i = 0; i < resolveInfos.size(); i++) {
				ResolveInfo resolveInfo = resolveInfos.get(i);
				ActivityInfo activityInfo = resolveInfo.activityInfo;
				String packageName = activityInfo.packageName;
				String className = activityInfo.name;
				pm.clearPackagePreferredActivities(packageName);
			}
		}
	}

	/**
	 * 获取所有默认的APP. 注意，如果只有一个，以前没有设置过.
	 */
	public List<ComponentName> getAllDefaultApp() {
		List<ComponentName> activities = new ArrayList<ComponentName>();
		List<IntentFilter> filters = new ArrayList<IntentFilter>();
		final IntentFilter filter = new IntentFilter(Intent.ACTION_VIEW);
		filters.add(filter);
		pm.getPreferredActivities(filters, activities, null);
		return activities;
	}

	/**
	 * 判断是否为默认启动项.
	 */
	public boolean isDefaultApp(String packName) {
		List<ComponentName> activities = getAllDefaultApp();
		for (ComponentName cn : activities) {
			String pn = cn.getPackageName();
			if (pn.equals(packName)) {
				return true;
			}
		}
		return false;
	}

}
