/*
 * Copyright (c) 2007 Pentaho Corporation.  All rights reserved. 
 * This software was Samatar Hassan and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.gnu.org/licenses/lgpl-2.1.txt. The Original Code is Pentaho 
 * Data Integration.  The Initial Developer is Samatar Hassan.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
*/

package org.pentaho.di.job.entries.syslog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.TimeZone;

import org.pentaho.di.core.Const;
import org.pentaho.di.i18n.BaseMessages;
import org.productivity.java.syslog4j.SyslogConstants;
import org.productivity.java.syslog4j.SyslogIF;


/**
 * This defines settings for Syslog.
 *
 * @author Samatar
 * @since 05-01-2010
 *
 */
public class SyslogDefs {
	
	private static Class<?> PKG = JobEntrySyslog.class; // for i18n purposes, needed by Translator2!!   $NON-NLS-1$

	public static final String DEFAULT_PROTOCOL_UDP = "udp";
	
	public static final int	DEFAULT_PORT = 514;
	public static final String DEFAULT_DATE_FORMAT = "MMM dd HH:mm:ss";

	static private Hashtable<String, Integer>	facHash;
	static private Hashtable<String, Integer>	priHash;

	public static final String[] FACILITYS = new String[] {
		"KERNEL",
		"USER",
		"MAIL",
		"DAEMON",
		"AUTH",
		"SYSLOG",
		"LPR",	
		"NEWS",
		"UUCP",
		"CRON",
		"LOCAL0",
		"LOCAL1",
		"LOCAL2",
		"LOCAL3",
		"LOCAL4",
		"LOCAL5",
		"LOCAL6",
		"LOCAL7",
	};
	public static final String[] PRIORITYS = new String[] {
		"EMERGENCY",
		"ALERT",
		"CRITICAL",
		"ERROR",
		"WARNING",
		"NOTICE",
		"INFO",
		"DEBUG",
	};

	static {
		facHash = new Hashtable<String, Integer>( 18 );
		facHash.put( "KERNEL",		SyslogConstants.FACILITY_KERN);
		facHash.put( "USER",		SyslogConstants.FACILITY_USER );
		facHash.put( "MAIL",		SyslogConstants.FACILITY_MAIL );
		facHash.put( "DAEMON",		SyslogConstants.FACILITY_DAEMON );
		facHash.put( "AUTH",		SyslogConstants.FACILITY_AUTH );
		facHash.put( "SYSLOG",		SyslogConstants.FACILITY_SYSLOG );
		facHash.put( "LPR",			SyslogConstants.FACILITY_LPR );
		facHash.put( "NEWS",		SyslogConstants.FACILITY_NEWS );
		facHash.put( "UUCP",		SyslogConstants.FACILITY_UUCP );
		facHash.put( "CRON",		SyslogConstants.FACILITY_CRON );
		facHash.put( "LOCAL0",		SyslogConstants.FACILITY_LOCAL0 );
		facHash.put( "LOCAL1",		SyslogConstants.FACILITY_LOCAL1 );
		facHash.put( "LOCAL2",		SyslogConstants.FACILITY_LOCAL2 );
		facHash.put( "LOCAL3",		SyslogConstants.FACILITY_LOCAL3);
		facHash.put( "LOCAL4",		SyslogConstants.FACILITY_LOCAL4);
		facHash.put( "LOCAL5",		SyslogConstants.FACILITY_LOCAL5);
		facHash.put( "LOCAL6",		SyslogConstants.FACILITY_LOCAL6);
		facHash.put( "LOCAL7",		SyslogConstants.FACILITY_LOCAL7);

		priHash = new Hashtable<String, Integer>( 8 );
		priHash.put( "EMERGENCY",		SyslogConstants.LEVEL_EMERGENCY);
		priHash.put( "ALERT",			SyslogConstants.LEVEL_ALERT);
		priHash.put( "CRITICAL",		SyslogConstants.LEVEL_CRITICAL);
		priHash.put( "ERROR",			SyslogConstants.LEVEL_ERROR);
		priHash.put( "WARNING",			SyslogConstants.LEVEL_WARN);
		priHash.put( "NOTICE",			SyslogConstants.LEVEL_NOTICE);
		priHash.put( "INFO",			SyslogConstants.LEVEL_INFO);
		priHash.put( "DEBUG",			SyslogConstants.LEVEL_DEBUG);
	}


	static public int computeCode( int facility, int priority ) {
		return ( (facility << 3) | priority );
	}

	static public int getPriority( String priority )throws SyslogException {
		Integer result = (Integer) SyslogDefs.priHash.get( priority );
		
		if ( result == null ){
			throw new SyslogException (BaseMessages.getString(PKG, "JobEntrySyslog.UnknownPriotity", priority));
		}

		return result.intValue();
	}

	static public int getFacility( String facility ) throws SyslogException {
		Integer result = (Integer) SyslogDefs.facHash.get( facility );

		if ( result == null ) {
			throw new SyslogException (BaseMessages.getString(PKG, "JobEntrySyslog.UnknownFacility", facility));
		}
		return result.intValue();
	}

	static public void sendMessage(SyslogIF syslog, int priority,String message, 
			boolean addTimestamp, String pattern,
			boolean addHostName) {
		
		String messageString =message;
		
		// Do we need to add hostname?
		if(addHostName) {
			messageString= Const.getHostname() + " " +messageString;
		}
		
		// Do we need to add timestamp
		if(addTimestamp) {
			SimpleDateFormat dateFormat = new SimpleDateFormat (pattern);
			dateFormat.setTimeZone(TimeZone.getDefault());
			messageString = dateFormat.format( Calendar.getInstance().getTime() ) + " : " + messageString;
		}

		// send message
		switch(priority) {
			case SyslogConstants.LEVEL_EMERGENCY  : syslog.emergency(messageString); break;
			case SyslogConstants.LEVEL_ALERT : syslog.alert(messageString); break;
			case SyslogConstants.LEVEL_CRITICAL : syslog.critical(messageString); break;
			case SyslogConstants.LEVEL_ERROR : syslog.error(messageString); break;
			case SyslogConstants.LEVEL_WARN : syslog.warn(messageString); break;
			case SyslogConstants.LEVEL_NOTICE : syslog.notice(messageString); break;
			case SyslogConstants.LEVEL_INFO : syslog.info(messageString); break;
			case SyslogConstants.LEVEL_DEBUG : syslog.debug(messageString); break;
			default: break;
		}
	}
 }
