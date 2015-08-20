#!/usr/bin/env ruby
# encoding: utf-8

require "bunny"

raise "Usage: push.rb number_of_messages" if ARGV[0].nil?

conn = Bunny.new(:hostname => 'docker', :port => 5001)
conn.start
channel = conn.create_channel
q = channel.queue("testqueue", :durable => false, :auto_delete => false)
 
for i in 0..ARGV[0].to_i
	channel.default_exchange.publish("Hello World! + #{i.to_s}", :routing_key => q.name)
	puts i if i % 10000 == 0
end

conn.close
