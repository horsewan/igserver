package com.dy.igserver.utils;

public class UniqueID {

        // ==============================Fields===========================================
        /**
         * 开始时间截 (2017-11-01)
         */
        public static final long twepoch = 1509465600000L;

        public static final long workerIdBits = 7L;

        public static final long datacenterIdBits = 1L;

        public static final long maxWorkerId = -1L ^ (-1L << workerIdBits);

        public static final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

        public static final long sequenceBits = 8L;

        public static final long placeHoldBits = 8L;

        public static final long sequenceIdShift = placeHoldBits;

        public static final long workerIdShift = sequenceBits + sequenceIdShift;

        public static final long dataCenterIdShift = workerIdShift + workerIdBits;

        public static final long timestampShift = dataCenterIdShift + datacenterIdBits;

        public static final long sequenceMask = -1L ^ (-1L << sequenceBits);

        private long workerId;

        private long datacenterId;

        private long sequence = 0L;

        private long lastTimestamp = -1L;

        //==============================Constructors=====================================
        public UniqueID(long workerId, long datacenterId) {
            if (workerId > maxWorkerId || workerId < 0) {
                throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
            }
            if (datacenterId > maxDatacenterId || datacenterId < 0) {
                throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
            }
            this.workerId = workerId;
            this.datacenterId = datacenterId;
        }

        // ==============================Methods==========================================

        /**
         * 获得下一个ID (该方法是线程安全的)
         *
         * @return SnowflakeId
         */
        public synchronized long nextId(long placeHolder) {
            long timestamp = currentMillis();

            //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
            if (timestamp < lastTimestamp) {
                throw new RuntimeException(
                        String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
            }

            //如果是同一时间生成的，则进行毫秒内序列
            if (lastTimestamp == timestamp) {
                sequence = (sequence + 1) & sequenceMask;
                //毫秒内序列溢出
                if (sequence == 0) {
                    //阻塞到下一个毫秒,获得新的时间戳
                    timestamp = tilNextMillis(lastTimestamp);
                }
            }
            //时间戳改变，毫秒内序列重置
            else {
                sequence = 0L;
            }

            //上次生成ID的时间截
            lastTimestamp = timestamp;


            //移位并通过或运算拼到一起组成64位的ID
            return ((timestamp - twepoch) << timestampShift) //
                    | (datacenterId << dataCenterIdShift) //
                    | (workerId << workerIdShift) //
                    | (sequence << sequenceIdShift)
                    | (0XFF & placeHolder);
        }

        protected long tilNextMillis(long lastTimestamp) {
            long timestamp = currentMillis();
            while (timestamp <= lastTimestamp) {
                timestamp = currentMillis();
            }
            return timestamp;
        }

        /**
         * 返回以毫秒为单位的当前时间
         *
         * @return 当前时间(毫秒)
         */
        protected long currentMillis() {
            return System.currentTimeMillis();
        }

        //==============================Test=============================================

        /**
         * 测试
         */
        public static void main(String[] args) {
            UniqueID idWorker = new UniqueID(1, 0);
            long id = idWorker.nextId(9);
            System.out.println(id);
        }
}
