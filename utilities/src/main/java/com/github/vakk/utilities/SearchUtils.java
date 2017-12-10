package com.github.vakk.utilities;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;


/**
 * Created by Valery Kotsulym on 12/6/17.
 */

public class SearchUtils {
    /**
     * Abstract logic of searching.
     * Represent logic for do searching in items collection.
     *
     * @param <T> type of searchable items.
     */
    public static abstract class SearchQuery<T> {

        private boolean mIgnoreSpaces;
        private boolean mIgnoreCase;
        private boolean mShowAllOnEmptyQuery;

        /**
         * Do search query.
         *
         * @param items collections to search.
         * @return result of searching.
         */
        public List<T> execute(String query, List<T> items) {

            query = adjustWithFlags(query);

            if (query.isEmpty()) {
                return isShowAllOnEmptyQuery() ? items : Collections.emptyList();
            }

            List<T> result = new ArrayList<>();

            for (T item : items) {
                List<String> tags = getItemTags(item);

                for (String searchData : tags) {
                    if (isRequiredData(query, adjustWithFlags(searchData))) {
                        if (!result.contains(item)) {
                            result.add(item);
                        }
                        break;
                    }
                }
            }

            return result;
        }

        private String adjustWithFlags(String text) {
            String result = text;
            if (isIgnoreCase()) {
                result = result.toLowerCase();
            }

            if (isIgnoreSpaces()) {
                result = result.trim();
            }

            return result;
        }

        private boolean isRequiredData(String query, String toCheck) {

            boolean contains = false;

            String substring = toCheck;
            int lastCharacterPosition = -1;

            for (int i = 0; i < query.length(); i++) {

                substring = substring.substring(lastCharacterPosition + 1);

                char currentValue = query.charAt(i);

                if (!substring.contains(String.valueOf(currentValue))) {
                    contains = false;
                    break;
                } else {
                    lastCharacterPosition = substring.indexOf(currentValue);
                    contains = true;
                }
            }

            return contains;
        }

        /**
         * Logic for receive item tags, description or someone else, which should be used for searching.
         *
         * @param item input item.
         * @return list of tags or someone which should be used to search.
         */
        public abstract
        @NonNull
        List<String> getItemTags(T item);

        public boolean isIgnoreSpaces() {
            return mIgnoreSpaces;
        }

        /**
         * Set ignore spaces logic.
         * For example:
         *
         * @param ignoreSpaces if true "some data" equals "somedata" else "some data" not equals "somedata".
         */
        public void setIgnoreSpaces(boolean ignoreSpaces) {
            mIgnoreSpaces = ignoreSpaces;
        }

        public boolean isIgnoreCase() {
            return mIgnoreCase;
        }

        /**
         * Set ignore cases logic.
         * For example:
         *
         * @param ignoreCase if true "SomeData" equals "somedata" else "SomeData" not equals "somedata".
         */
        public void setIgnoreCase(boolean ignoreCase) {
            mIgnoreCase = ignoreCase;
        }


        public boolean isShowAllOnEmptyQuery() {
            return mShowAllOnEmptyQuery;
        }

        /**
         * Set show all on query empty.
         *
         * @param showAllOnEmptyQuery if true - query "" would show all items or show nothing otherwise.
         */
        public void setShowAllOnEmptyQuery(boolean showAllOnEmptyQuery) {
            mShowAllOnEmptyQuery = showAllOnEmptyQuery;
        }
    }


    /**
     * Abstract logic of searching based on RX.
     * Represent logic for do searching in items collection.
     *
     * @param <T> type of searchable items.
     */
    public static abstract class RxSearchQuery<T> {

        private boolean mIgnoreSpaces;
        private boolean mIgnoreCase;
        private boolean mShowAllOnEmptyQuery;

        /**
         * Do search query.
         *
         * @param items collections to search.
         * @return result of searching.
         */
        public Observable<T> search(String query, List<T> items) {
            return Observable.fromIterable(items)
                    .flatMap(item -> isRequired(query, item)
                            .filter(required -> required)
                            .map(r -> item)
                    );
        }

        public Observable<Boolean> isRequired(String query, T item) {
            String searchQuery = adjustWithFlags(query);
            return Observable.fromIterable(getItemTags(item))
                    .map(this::adjustWithFlags)
                    .map(tag -> mShowAllOnEmptyQuery && searchQuery.isEmpty() || isRequired(searchQuery, tag))
                    .filter(r -> r)
                    .toList()
                    .toObservable()
                    .map(res -> res.size() > 0 ? true : false);
        }

        private boolean isRequired(String query, String tag) {
            boolean contains = false;

            String substring = tag;
            int lastCharacterPosition = -1;

            for (int i = 0; i < query.length(); i++) {

                substring = substring.substring(lastCharacterPosition + 1);

                char currentValue = query.charAt(i);

                if (!substring.contains(String.valueOf(currentValue))) {
                    contains = false;
                    break;
                } else {
                    lastCharacterPosition = substring.indexOf(currentValue);
                    contains = true;
                }
            }

            return contains;
        }

        private String adjustWithFlags(String text) {
            String result = text;
            if (isIgnoreCase()) {
                result = result.toLowerCase();
            }

            if (isIgnoreSpaces()) {
                result = result.trim();
            }

            return result;
        }

        /**
         * Logic for receive item tags, description or someone else, which should be used for searching.
         *
         * @param item input item.
         * @return list of tags or someone which should be used to search.
         */
        public abstract
        @NonNull
        List<String> getItemTags(T item);

        public boolean isIgnoreSpaces() {
            return mIgnoreSpaces;
        }

        /**
         * Set ignore spaces logic.
         * For example:
         *
         * @param ignoreSpaces if true "some data" equals "somedata" else "some data" not equals "somedata".
         */
        public void setIgnoreSpaces(boolean ignoreSpaces) {
            mIgnoreSpaces = ignoreSpaces;
        }

        public boolean isIgnoreCase() {
            return mIgnoreCase;
        }

        /**
         * Set ignore cases logic.
         * For example:
         *
         * @param ignoreCase if true "SomeData" equals "somedata" else "SomeData" not equals "somedata".
         */
        public void setIgnoreCase(boolean ignoreCase) {
            mIgnoreCase = ignoreCase;
        }


        public boolean isShowAllOnEmptyQuery() {
            return mShowAllOnEmptyQuery;
        }

        /**
         * Set show all on query empty.
         *
         * @param showAllOnEmptyQuery if true - query "" would show all items or show nothing otherwise.
         */
        public void setShowAllOnEmptyQuery(boolean showAllOnEmptyQuery) {
            mShowAllOnEmptyQuery = showAllOnEmptyQuery;
        }
    }
}
