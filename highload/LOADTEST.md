# Этап 3. Нагрузочное тестирование и оптимизация
```
Нагрузку подавали с помощью инструмента нагрузочного тесирования - wrk
Подавали запросы только PUT: соединения = потоки = 1, 2, 4 c/без перезаписи
Подавали запросы только GET: соединения = потоки = 1, 2, 4 на большом наборе ключей с/без повторов
Для запросов GET на сервер заранее были загружены 250000 файлов
Все нагрузки подавали длительностью 1 минута
```
## До оптимизации
### PUT без перезаписи

#### соединения = потоки = 1
```
wrk --latency -c1 -t1 -d1m -s scripts/unique_put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    19.21ms   35.94ms 347.69ms   89.95%
    Req/Sec    82.00     30.21   121.00     78.90%
  Latency Distribution
     50%    5.84ms
     75%    7.58ms
     90%   55.26ms
     99%  184.42ms
  4736 requests in 1.00m, 434.75KB read
Requests/sec:     78.82
Transfer/sec:      7.24KB
```

#### соединения = потоки = 2
```
wrk --latency -c2 -t2 -d1m -s scripts/unique_put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    20.17ms   38.09ms 372.27ms   90.22%
    Req/Sec    76.40     29.00   120.00     68.97%
  Latency Distribution
     50%    6.08ms
     75%    8.88ms
     90%   57.40ms
     99%  197.03ms
  8819 requests in 1.00m, 809.56KB read
Requests/sec:    146.79
Transfer/sec:     13.47KB
```

#### соединения = потоки = 4
```
wrk --latency -c4 -t4 -d1m -s scripts/unique_put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    19.96ms   37.28ms 350.89ms   90.08%
    Req/Sec    76.38     29.04   121.00     68.69%
  Latency Distribution
     50%    6.22ms
     75%    9.40ms
     90%   56.89ms
     99%  192.30ms
  17639 requests in 1.00m, 1.58MB read
Requests/sec:    293.51
Transfer/sec:     26.94KB
```

### PUT c перезаписью

#### соединения = потоки = 1
```
wrk --latency -c1 -t1 -d1m -s scripts/repeated_put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    20.19ms   37.65ms 342.31ms   89.89%
    Req/Sec    78.28     29.84   121.00     68.32%
  Latency Distribution
     50%    6.09ms
     75%    8.66ms
     90%   58.60ms
     99%  192.23ms
  4509 requests in 1.00m, 413.91KB read
Requests/sec:     75.07
Transfer/sec:      6.89KB
```

#### соединения = потоки = 2
```
wrk --latency -c2 -t2 -d1m -s scripts/repeated_put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    19.39ms   36.44ms 343.02ms   89.88%
    Req/Sec    80.96     30.39   121.00     77.43%
  Latency Distribution
     50%    5.69ms
     75%    7.55ms
     90%   56.97ms
     99%  187.54ms
  9365 requests in 1.00m, 859.68KB read
Requests/sec:    155.87
Transfer/sec:     14.31KB
```

#### соединения = потоки = 4
```
wrk --latency -c4 -t4 -d1m -s scripts/repeated_put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    19.68ms   36.99ms 349.90ms   89.93%
    Req/Sec    78.40     29.93   121.00     70.27%
  Latency Distribution
     50%    5.94ms
     75%    8.76ms
     90%   57.04ms
     99%  191.36ms
  18167 requests in 1.00m, 1.63MB read
Requests/sec:    302.28
Transfer/sec:     27.75KB
```

### GET без повторов

#### соединения = потоки = 1
```
wrk --latency -c1 -t1 -d1m -s scripts/unique_get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    13.93ms   40.56ms 443.68ms   91.50%
    Req/Sec     2.11k     3.41k    8.67k    74.87%
  Latency Distribution
     50%  137.00us
     75%    4.52ms
     90%   42.51ms
     99%  206.45ms
  117380 requests in 1.00m, 456.50MB read
  Non-2xx or 3xx responses: 1
Requests/sec:   1955.76
Transfer/sec:      7.61MB
```

#### соединения = потоки = 2
```
wrk --latency -c2 -t2 -d1m -s scripts/unique_get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    12.35ms   47.93ms 737.06ms   94.02%
    Req/Sec     2.20k     2.43k    5.38k    60.60%
  Latency Distribution
     50%  201.00us
     75%    3.53ms
     90%   27.89ms
     99%  206.52ms
  249655 requests in 1.00m, 0.95GB read
  Non-2xx or 3xx responses: 3
Requests/sec:   4154.64
Transfer/sec:     16.16MB
```

#### соединения = потоки = 4
```
wrk --latency -c4 -t4 -d1m -s scripts/unique_get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     5.51ms   25.82ms 465.25ms   95.58%
    Req/Sec     2.18k     1.13k    3.08k    77.30%
  Latency Distribution
     50%  376.00us
     75%  507.00us
     90%    5.42ms
     99%  115.53ms
  513081 requests in 1.00m, 1.95GB read
  Non-2xx or 3xx responses: 7
Requests/sec:   8545.38
Transfer/sec:     33.23MB
```

### GET c повторами

#### соединения = потоки = 1
```
wrk --latency -c1 -t1 -d1m -s scripts/repeated_get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   147.79us  553.34us  28.66ms   98.97%
    Req/Sec     8.76k   678.95     9.53k    88.33%
  Latency Distribution
     50%  101.00us
     75%  104.00us
     90%  123.00us
     99%  797.00us
  522763 requests in 1.00m, 1.99GB read
  Non-2xx or 3xx responses: 104
Requests/sec:   8712.62
Transfer/sec:     33.88MB
```

#### соединения = потоки = 2
```
wrk --latency -c2 -t2 -d1m -s scripts/repeated_get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   226.67us  508.31us  33.28ms   98.73%
    Req/Sec     5.14k   398.96     5.58k    87.83%
  Latency Distribution
     50%  178.00us
     75%  184.00us
     90%  205.00us
     99%    1.26ms
  613631 requests in 1.00m, 2.33GB read
  Non-2xx or 3xx responses: 123
Requests/sec:  10226.76
Transfer/sec:     39.76MB
```

#### соединения = потоки = 4
```
wrk --latency -c4 -t4 -d1m -s scripts/repeated_get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   400.34us  622.39us  42.91ms   98.53%
    Req/Sec     2.78k   230.63     3.48k    87.81%
  Latency Distribution
     50%  338.00us
     75%  422.00us
     90%  504.00us
     99%    2.14ms
  663796 requests in 1.00m, 2.52GB read
  Non-2xx or 3xx responses: 135
Requests/sec:  11044.92
Transfer/sec:     42.95MB
```

## После оптимизации
### PUT без перезаписи

#### соединения = потоки = 1
```
wrk --latency -c1 -t1 -d1m -s scripts/unique_put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    22.54ms   43.38ms 438.17ms   90.31%
    Req/Sec    73.10     27.52   111.00     71.89%
  Latency Distribution
     50%    6.63ms
     75%    9.56ms
     90%   64.34ms
     99%  224.24ms
  4178 requests in 1.00m, 383.53KB read
Requests/sec:     69.55
Transfer/sec:      6.38KB
```

#### соединения = потоки = 2
```
wrk --latency -c2 -t2 -d1m -s scripts/unique_put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    20.48ms   38.31ms 362.51ms   90.08%
    Req/Sec    75.40     28.12   111.00     75.16%
  Latency Distribution
     50%    6.23ms
     75%    8.46ms
     90%   58.15ms
     99%  197.62ms
  8705 requests in 1.00m, 799.09KB read
Requests/sec:    144.89
Transfer/sec:     13.30KB
```

#### соединения = потоки = 4
```
wrk --latency -c4 -t4 -d1m -s scripts/unique_put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    20.77ms   38.80ms 355.16ms   90.09%
    Req/Sec    73.70     27.97   111.00     75.03%
  Latency Distribution
     50%    6.55ms
     75%   10.27ms
     90%   59.06ms
     99%  203.03ms
  16975 requests in 1.00m, 1.52MB read
Requests/sec:    282.69
Transfer/sec:     25.95KB
```

### PUT c перезаписью

#### соединения = потоки = 1
```
wrk --latency -c1 -t1 -d1m -s scripts/repeated_put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    20.56ms   38.67ms 361.72ms   90.04%
    Req/Sec    76.63     29.30   121.00     68.74%
  Latency Distribution
     50%    6.19ms
     75%    8.57ms
     90%   58.77ms
     99%  200.51ms
  4409 requests in 1.00m, 404.73KB read
Requests/sec:     73.47
Transfer/sec:      6.74KB
```

#### соединения = потоки = 2
```
wrk --latency -c2 -t2 -d1m -s scripts/repeated_put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    20.03ms   38.00ms 365.09ms   90.16%
    Req/Sec    78.26     29.71   121.00     67.84%
  Latency Distribution
     50%    5.99ms
     75%    8.05ms
     90%   56.96ms
     99%  195.83ms
  9044 requests in 1.00m, 830.21KB read
Requests/sec:    150.55
Transfer/sec:     13.82KB
```

#### соединения = потоки = 4
```
wrk --latency -c4 -t4 -d1m -s scripts/repeated_put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    20.52ms   39.13ms 364.07ms   90.18%
    Req/Sec    76.96     29.12   121.00     70.22%
  Latency Distribution
     50%    6.18ms
     75%    9.33ms
     90%   58.69ms
     99%  202.68ms
  17705 requests in 1.00m, 1.59MB read
Requests/sec:    294.63
Transfer/sec:     27.05KB
```

### GET без повторов

#### соединения = потоки = 1
```
wrk --latency -c1 -t1 -d1m -s scripts/unique_get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    13.03ms   38.15ms 437.27ms   91.85%
    Req/Sec     2.03k     3.37k    8.93k    75.81%
  Latency Distribution
     50%  142.00us
     75%    4.66ms
     90%   37.57ms
     99%  201.18ms
  112722 requests in 1.00m, 438.38MB read
  Non-2xx or 3xx responses: 1
Requests/sec:   1876.72
Transfer/sec:      7.30MB
```

#### соединения = потоки = 2
```
wrk --latency -c2 -t2 -d1m -s scripts/unique_get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    14.15ms   40.90ms 447.94ms   91.13%
    Req/Sec     2.19k     2.66k    6.24k    65.83%
  Latency Distribution
     50%  189.00us
     75%    4.15ms
     90%   45.73ms
     99%  213.78ms
  242205 requests in 1.00m, 0.92GB read
  Non-2xx or 3xx responses: 3
Requests/sec:   4036.40
Transfer/sec:     15.70MB
```

#### соединения = потоки = 4
```
wrk --latency -c4 -t4 -d1m -s scripts/unique_get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    10.51ms   35.57ms 448.14ms   92.97%
    Req/Sec     2.23k     1.64k    4.00k    62.26%
  Latency Distribution
     50%  315.00us
     75%    1.88ms
     90%   20.84ms
     99%  188.60ms
  507527 requests in 1.00m, 1.93GB read
  Non-2xx or 3xx responses: 7
Requests/sec:   8448.11
Transfer/sec:     32.85MB
```

### GET c повторами

#### соединения = потоки = 1
```
wrk --latency -c1 -t1 -d1m -s scripts/repeated_get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   122.11us  433.64us  15.17ms   98.51%
    Req/Sec    11.78k     1.16k   12.85k    94.67%
  Latency Distribution
     50%   70.00us
     75%   73.00us
     90%   98.00us
     99%    1.91ms
  702975 requests in 1.00m, 2.67GB read
  Non-2xx or 3xx responses: 140
Requests/sec:  11715.37
Transfer/sec:     45.55MB
```

#### соединения = потоки = 2
```
wrk --latency -c2 -t2 -d1m -s scripts/repeated_get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   178.72us  479.68us  21.75ms   98.19%
    Req/Sec     7.54k   716.47     8.10k    92.42%
  Latency Distribution
     50%  118.00us
     75%  120.00us
     90%  141.00us
     99%    2.68ms
  900258 requests in 1.00m, 3.42GB read
  Non-2xx or 3xx responses: 180
Requests/sec:  15003.66
Transfer/sec:     58.34MB
```

#### соединения = потоки = 4
```
wrk --latency -c4 -t4 -d1m -s scripts/repeated_get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   290.49us  567.63us  33.57ms   97.89%
    Req/Sec     4.25k   444.18     5.75k    88.72%
  Latency Distribution
     50%  217.00us
     75%  272.00us
     90%  326.00us
     99%    3.24ms
  1016529 requests in 1.00m, 3.86GB read
  Non-2xx or 3xx responses: 203
Requests/sec:  16914.26
Transfer/sec:     65.77MB
```

## Результат оптимизации
```
Был реализован кэш на 5000 записей. За счет этого был получили выигрыш в чтении с повторами, но получили меньшую производительность в остальных случаях. В PUT с/без перезаписи и GET без повторов кэшируются записи и никогда не используются для чтения.
```
|Тип теста|До оптимизации Req/Sec(1, 2, 4)|После оптимизации Req/Sec(1, 2, 4)|
|---------|--------------|-----------------|
|unique_put|82.00 76.40 76.38|73.10 75.40 73.70|
|repeated_put|78.28 80.96 78.40|76.63 78.26 76.96|
|unique_get|2.11k 2.20k 2.18k|2.03k 2.19k 2.23k|
|repeated_get|8.76k 5.14k 2.78k|11.78k 7.54k 4.25k|