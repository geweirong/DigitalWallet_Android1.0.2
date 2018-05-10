package com.innext.szqb.ui.my.bean;

import java.util.List;

/**
 * Created by hengxinyongli on 2016/12/5 0005.
 */
public class Lottery {
    /**
     * page : 1
     * pageTotal : 1
     * item : [{"periods":1,"awardMoney":null,"awardId":"21226","status":0,"userId":"17173","luckyDraw":10020873,"addTime":{"date":7,"day":6,"hours":18,"minutes":14,"month":0,"seconds":54,"time":1483784094000,"timezoneOffset":-480,"year":117},"stepName":"认证成功"},{"periods":1,"awardMoney":null,"awardId":"18818","status":0,"userId":"17173","luckyDraw":10018482,"addTime":{"date":7,"day":6,"hours":14,"minutes":53,"month":0,"seconds":27,"time":1483772007000,"timezoneOffset":-480,"year":117},"stepName":"认证成功"},{"periods":1,"awardMoney":null,"awardId":"18816","status":0,"userId":"17173","luckyDraw":10018480,"addTime":{"date":7,"day":6,"hours":14,"minutes":53,"month":0,"seconds":13,"time":1483771993000,"timezoneOffset":-480,"year":117},"stepName":"认证成功"},{"periods":1,"awardMoney":null,"awardId":"18596","status":0,"userId":"17173","luckyDraw":10018262,"addTime":{"date":7,"day":6,"hours":14,"minutes":34,"month":0,"seconds":39,"time":1483770879000,"timezoneOffset":-480,"year":117},"stepName":"认证成功"}]
     * pageSize : 10
     */

    private int page;
    private int pageTotal;
    private int pageSize;
    private List<ItemBean> item;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<ItemBean> getItem() {
        return item;
    }

    public void setItem(List<ItemBean> item) {
        this.item = item;
    }

    public static class ItemBean {
        /**
         * periods : 1
         * awardMoney : null
         * awardId : 21226
         * status : 0
         * userId : 17173
         * luckyDraw : 10020873
         * addTime : {"date":7,"day":6,"hours":18,"minutes":14,"month":0,"seconds":54,"time":1483784094000,"timezoneOffset":-480,"year":117}
         * stepName : 认证成功
         */

        private int periods;
        private Object awardMoney;
        private String awardId;
        private int status;
        private String userId;
        private int luckyDraw;
        private AddTimeBean addTime;
        private String stepName;

        public int getPeriods() {
            return periods;
        }

        public void setPeriods(int periods) {
            this.periods = periods;
        }

        public Object getAwardMoney() {
            return awardMoney;
        }

        public void setAwardMoney(Object awardMoney) {
            this.awardMoney = awardMoney;
        }

        public String getAwardId() {
            return awardId;
        }

        public void setAwardId(String awardId) {
            this.awardId = awardId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getLuckyDraw() {
            return luckyDraw;
        }

        public void setLuckyDraw(int luckyDraw) {
            this.luckyDraw = luckyDraw;
        }

        public AddTimeBean getAddTime() {
            return addTime;
        }

        public void setAddTime(AddTimeBean addTime) {
            this.addTime = addTime;
        }

        public String getStepName() {
            return stepName;
        }

        public void setStepName(String stepName) {
            this.stepName = stepName;
        }

        public static class AddTimeBean {
            /**
             * date : 7
             * day : 6
             * hours : 18
             * minutes : 14
             * month : 0
             * seconds : 54
             * time : 1483784094000
             * timezoneOffset : -480
             * year : 117
             */

            private int date;
            private int day;
            private int hours;
            private int minutes;
            private int month;
            private int seconds;
            private long time;
            private int timezoneOffset;
            private int year;

            public int getDate() {
                return date;
            }

            public void setDate(int date) {
                this.date = date;
            }

            public int getDay() {
                return day;
            }

            public void setDay(int day) {
                this.day = day;
            }

            public int getHours() {
                return hours;
            }

            public void setHours(int hours) {
                this.hours = hours;
            }

            public int getMinutes() {
                return minutes;
            }

            public void setMinutes(int minutes) {
                this.minutes = minutes;
            }

            public int getMonth() {
                return month;
            }

            public void setMonth(int month) {
                this.month = month;
            }

            public int getSeconds() {
                return seconds;
            }

            public void setSeconds(int seconds) {
                this.seconds = seconds;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public int getTimezoneOffset() {
                return timezoneOffset;
            }

            public void setTimezoneOffset(int timezoneOffset) {
                this.timezoneOffset = timezoneOffset;
            }

            public int getYear() {
                return year;
            }

            public void setYear(int year) {
                this.year = year;
            }
        }
    }
        /*private int page;
        private int pageTotal;
        private int pageSize;
        *//**
         * periods : 16
         * awardMoney : 5000
         * awardId : 4862fb9c6f064761a4992cfe716dcf03
         * status : 0
         * userId : a5943575d63748b8a179a96bf01f29fa
         * luckyDraw : 10000001
         * addTime : {"time":1480736670000,"minutes":44,"seconds":30,"hours":11,"month":11,"year":116,"timezoneOffset":-480,"day":6,"date":3}
         * stepName : 认证成功
         *//*

        private List<ItemBean> item;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPageTotal() {
            return pageTotal;
        }

        public void setPageTotal(int pageTotal) {
            this.pageTotal = pageTotal;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public List<ItemBean> getItem() {
            return item;
        }

        public void setItem(List<ItemBean> item) {
            this.item = item;
        }

        public static class ItemBean {
            private String periods;
            private String awardMoney;
            private String awardId;
            private int status;
            private String userId;
            private String luckyDraw;
            *//**
             * time : 1480736670000
             * minutes : 44
             * seconds : 30
             * hours : 11
             * month : 11
             * year : 116
             * timezoneOffset : -480
             * day : 6
             * date : 3
             *//*

            private AddTimeBean addTime;
            private String stepName;

            public String getPeriods() {
                return periods;
            }

            public void setPeriods(String periods) {
                this.periods = periods;
            }

            public String getAwardMoney() {
                return awardMoney;
            }

            public void setAwardMoney(String awardMoney) {
                this.awardMoney = awardMoney;
            }

            public String getAwardId() {
                return awardId;
            }

            public void setAwardId(String awardId) {
                this.awardId = awardId;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getLuckyDraw() {
                return luckyDraw;
            }

            public void setLuckyDraw(String luckyDraw) {
                this.luckyDraw = luckyDraw;
            }

            public AddTimeBean getAddTime() {
                return addTime;
            }

            public void setAddTime(AddTimeBean addTime) {
                this.addTime = addTime;
            }

            public String getStepName() {
                return stepName;
            }

            public void setStepName(String stepName) {
                this.stepName = stepName;
            }

            public static class AddTimeBean {
                private long time;
                private int minutes;
                private int seconds;
                private int hours;
                private int month;
                private int year;
                private int timezoneOffset;
                private int day;
                private int date;

                public long getTime() {
                    return time;
                }

                public void setTime(long time) {
                    this.time = time;
                }

                public int getMinutes() {
                    return minutes;
                }

                public void setMinutes(int minutes) {
                    this.minutes = minutes;
                }

                public int getSeconds() {
                    return seconds;
                }

                public void setSeconds(int seconds) {
                    this.seconds = seconds;
                }

                public int getHours() {
                    return hours;
                }

                public void setHours(int hours) {
                    this.hours = hours;
                }

                public int getMonth() {
                    return month;
                }

                public void setMonth(int month) {
                    this.month = month;
                }

                public int getYear() {
                    return year;
                }

                public void setYear(int year) {
                    this.year = year;
                }

                public int getTimezoneOffset() {
                    return timezoneOffset;
                }

                public void setTimezoneOffset(int timezoneOffset) {
                    this.timezoneOffset = timezoneOffset;
                }

                public int getDay() {
                    return day;
                }

                public void setDay(int day) {
                    this.day = day;
                }

                public int getDate() {
                    return date;
                }

                public void setDate(int date) {
                    this.date = date;
                }
            }
        }*/
}

