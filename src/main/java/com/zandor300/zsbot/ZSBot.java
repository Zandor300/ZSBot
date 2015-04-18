/**
 * Copyright 2015 Zandor Smith
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zandor300.zsbot;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

import java.io.IOException;

/**
 * @author Zandor Smith
 * @since 1.0.0
 */
public class ZSBot extends PircBot {

	public static void main(String[] args) {
		try {
			String password = "";
			for(String arg : args)
				if (arg.startsWith("-pass"))
					password = arg.replaceAll("-pass", "");

			if(password == "")
				throw new IllegalArgumentException("No password specified!");

			ZSBot bot = new ZSBot(password);
			bot.join("zsmcw", "zsmcp", "zsmc");
			System.out.println("Joined!");
		} catch(IrcException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public ZSBot(String password) throws IrcException, IOException {
		setName("ZSBot");
		setVerbose(true);
		connect("irc.esper.net", 6667, password);
	}

	public void join(String... channels) {
		for(String channel : channels)
			joinChannel("#" + channel);
	}

	@Override
	protected void onMessage(String channel, String sender, String login, String hostname, String message) {
		if(message.equalsIgnoreCase("!twitter")) {
			sendMessage(channel, "Twitter: http://twitter.com/Zandor300");
		} else if(message.equalsIgnoreCase("!wiki")) {
			if (channel.equalsIgnoreCase("#zsmcw"))
				sendMessage(channel, "Wiki: http://wiki.zsinfo.nl/wiki/index.php/ZSMCW");
			else if (channel.equalsIgnoreCase("#zsmcp"))
				sendMessage(channel, "Wiki: Not made yet...");
			else if (channel.equalsIgnoreCase("#zsmc")) {
				sendMessage(channel, "ZSMCW Wiki: http://wiki.zsinfo.nl/wiki/index.php/ZSMCW");
				sendMessage(channel, "ZSMCP Wiki: Not made yet...");
			}
		}
	}

	@Override
	protected void onJoin(final String channel, String sender, String login, String hostname) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(5000l);
					if (channel.equalsIgnoreCase("#zsmcw"))
						setTopic(channel, "ZSMCW - Minecraft Server Wrapper");
					else if (channel.equalsIgnoreCase("#zsmcp"))
						setTopic(channel, "ZSMCP - Minecraft Server Control Panel");
					else if (channel.equalsIgnoreCase("#zsmc"))
						setTopic(channel, "ZSMC - Minecraft Server Control - ZSMCW - ZSMCP");
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
