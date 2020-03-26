package client.userInfo;

import java.awt.*;

class GBC extends GridBagConstraints {//GridBagLayout的组件限制器

    GBC(int gridx, int gridy, int gridheight, int gridwidth) {
        super();
        super.gridx = gridx;//方格左上角单元格所在行号
        super.gridy = gridy;//方格左上角单元格所在列号
        super.gridheight = gridheight;//方格在横向占用的单元格数
        super.gridwidth = gridwidth;//方格在纵向占用的单元格数
    }

    GBC setWeight(double x, double y) {//占表格行列的比例
        super.weightx = x;
        super.weighty = y;
        return this;
    }

    GBC setFill(int x) {//组件的填充方式
        super.fill = x;
        return this;
    }

    GBC setAnchor(int anchor) {//组件在方格的位置
        super.anchor = anchor;
        return this;
    }
}