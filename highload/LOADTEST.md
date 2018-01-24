# Этап 3. Нагрузочное тестирование и оптимизация
Нагрузку подавали с помощью инструмента нагрузочного тесирования - wrk
Подавали запросы только PUT: соединения = потоки = 1, 2, 4 c/без перезаписи
Подавали запросы только GET: соединения = потоки = 1, 2, 4 на большом наборе ключей с/без повторов
Для запросов GET на сервер заранее были загружены 150000 файлов
Все нагрузки подавали длительностью 1 минута
## До оптимизации
### PUT без перезаписи

#### соединения = потоки = 1
wrk --latency -c1 -t1 -d1m -s scripts/unique_put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    20.06ms   39.19ms 414.98ms   90.42%
    Req/Sec    80.80     31.23   131.00     75.75%
  Latency Distribution
     50%    5.96ms
     75%    8.19ms
     90%   56.22ms
     99%  197.09ms
  4658 requests in 1.00m, 427.59KB read
Requests/sec:     77.57
Transfer/sec:      7.12KB

#### соединения = потоки = 2
wrk --latency -c2 -t2 -d1m -s scripts/unique_put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    20.54ms   39.79ms 436.87ms   90.53%
    Req/Sec    76.16     28.95   121.00     69.80%
  Latency Distribution
     50%    6.10ms
     75%    8.86ms
     90%   57.73ms
     99%  208.26ms
  8780 requests in 1.00m, 805.98KB read
Requests/sec:    146.14
Transfer/sec:     13.42KB

#### соединения = потоки = 4
wrk --latency -c4 -t4 -d1m -s scripts/unique_put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    22.02ms   43.34ms 447.29ms   90.46%
    Req/Sec    73.91     28.76   111.00     72.78%
  Latency Distribution
     50%    6.40ms
     75%   10.66ms
     90%   61.55ms
     99%  227.88ms
  16939 requests in 1.00m, 1.52MB read
Requests/sec:    282.02
Transfer/sec:     25.89KB

### PUT c перезаписью

#### соединения = потоки = 1
wrk --latency -c1 -t1 -d1m -s scripts/repeated_put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    20.86ms   39.69ms 367.95ms   89.99%
    Req/Sec    77.22     29.14   118.00     67.62%
  Latency Distribution
     50%    6.09ms
     75%    8.85ms
     90%   60.56ms
     99%  205.71ms
  4444 requests in 1.00m, 407.95KB read
Requests/sec:     73.96
Transfer/sec:      6.79KB

#### соединения = потоки = 2
wrk --latency -c2 -t2 -d1m -s scripts/repeated_put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    20.57ms   39.15ms 371.49ms   89.95%
    Req/Sec    77.39     28.78   111.00     69.02%
  Latency Distribution
     50%    5.96ms
     75%    8.35ms
     90%   60.35ms
     99%  202.71ms
  8887 requests in 1.00m, 815.89KB read
Requests/sec:    148.03
Transfer/sec:     13.59KB

#### соединения = потоки = 4
wrk --latency -c4 -t4 -d1m -s scripts/repeated_put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    20.68ms   39.72ms 369.38ms   89.92%
    Req/Sec    78.65     30.07   121.00     70.24%
  Latency Distribution
     50%    6.00ms
     75%    8.37ms
     90%   60.84ms
     99%  207.89ms
  18046 requests in 1.00m, 1.62MB read
Requests/sec:    300.39
Transfer/sec:     27.58KB

### GET без повторов

#### соединения = потоки = 1
wrk --latency -c1 -t1 -d1m -s scripts/unique_get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    14.31ms   42.63ms 468.88ms   91.86%
    Req/Sec     2.01k     3.31k    8.69k    75.91%
  Latency Distribution
     50%  145.00us
     75%    4.28ms
     90%   43.18ms
     99%  227.03ms
  110444 requests in 1.00m, 429.52MB read
  Non-2xx or 3xx responses: 1
Requests/sec:   1840.60
Transfer/sec:      7.16MB

#### соединения = потоки = 2
wrk --latency -c2 -t2 -d1m -s scripts/unique_get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    11.89ms   34.73ms 338.32ms   91.64%
    Req/Sec     2.02k     2.37k    5.18k    61.34%
  Latency Distribution
     50%  202.00us
     75%    3.67ms
     90%   34.83ms
     99%  185.42ms
  229112 requests in 1.00m, 0.87GB read
  Non-2xx or 3xx responses: 3
Requests/sec:   3812.71
Transfer/sec:     14.83MB

#### соединения = потоки = 4
wrk --latency -c4 -t4 -d1m -s scripts/unique_get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     7.21ms   28.57ms 457.64ms   94.31%
    Req/Sec     2.00k     1.16k    3.04k    73.27%
  Latency Distribution
     50%  410.00us
     75%  567.00us
     90%    8.73ms
     99%  133.47ms
  464670 requests in 1.00m, 1.76GB read
  Non-2xx or 3xx responses: 7
Requests/sec:   7737.48
Transfer/sec:     30.09MB

### GET c повторами

#### соединения = потоки = 1
wrk --latency -c1 -t1 -d1m -s scripts/repeated_get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     3.00ms    7.29ms  44.47ms   87.65%
    Req/Sec     7.01k     1.10k    9.47k    70.00%
  Latency Distribution
     50%   98.00us
     75%  127.00us
     90%   14.40ms
     99%   30.17ms
  418813 requests in 1.00m, 1.59GB read
  Non-2xx or 3xx responses: 418
Requests/sec:   6975.58
Transfer/sec:     27.10MB

#### соединения = потоки = 2
wrk --latency -c2 -t2 -d1m -s scripts/repeated_get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     3.49ms    7.95ms  64.20ms   86.90%
    Req/Sec     3.85k     1.29k    5.49k    56.25%
  Latency Distribution
     50%  180.00us
     75%  226.00us
     90%   16.53ms
     99%   31.30ms
  460054 requests in 1.00m, 1.75GB read
  Non-2xx or 3xx responses: 460
Requests/sec:   7662.86
Transfer/sec:     29.77MB

#### соединения = потоки = 4
wrk --latency -c4 -t4 -d1m -s scripts/repeated_get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     3.91ms    8.35ms 110.95ms   86.44%
    Req/Sec     2.06k     0.90k    3.12k    70.03%
  Latency Distribution
     50%  377.00us
     75%  521.00us
     90%   17.81ms
     99%   31.79ms
  493118 requests in 1.00m, 1.87GB read
  Non-2xx or 3xx responses: 495
Requests/sec:   8212.31
Transfer/sec:     31.91MB