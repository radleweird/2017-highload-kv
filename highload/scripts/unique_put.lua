counter = 0
wrk.method = "PUT"

request = function()
   path = "/v0/entity?id=" .. counter
   counter = counter + 1
   wrk.body = randomData(4000)
   return wrk.format(nil, path)
end

function randomData(length)
   data = ""
   for idx = 1, length do
      data = data .. math.random(0, 9)
   end
   return data
end