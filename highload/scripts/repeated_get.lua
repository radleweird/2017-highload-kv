counter = 0
wrk.method = "GET"
repeat_each = 10000

request = function()
   path = "/v0/entity?id=" .. counter
   counter = (counter + 1) % repeat_each
   return wrk.format(nil, path)
end