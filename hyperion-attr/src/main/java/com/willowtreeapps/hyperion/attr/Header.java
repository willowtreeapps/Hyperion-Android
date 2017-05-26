package com.willowtreeapps.hyperion.attr;

class Header implements AttributeDetailItem {

    private final String text;

    Header(String text) {
        this.text = text;
    }

    String getText() {
        return this.text;
    }

    @Override
    public int getViewType() {
        return AttributeDetailView.ITEM_HEADER;
    }

}