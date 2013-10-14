/*
 * Copyright (c) 1995-2008 Uwe B. Meding <uwe@uwemeding.com>
 *
 * This file is part of DTDC
 * This PCA software is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DTDC is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DTDC.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.uwemeding.dtdc_rt;

/**
 * This class implements the notations of a DTD. This class is typically used
 * through the Compiled DTD interfaces. Normal applications should have no need
 * to construct notations on their own.
 */
public class XMLNotation {

	private String name;
	private External externalId;

	/**
	 * Constructor
	 *
	 * @param name is the name of this notation
	 */
	public XMLNotation(String name, External externalId) {
		this.name = name;
		this.externalId = externalId;
	}

	/**
	 * Get the name of this notation.
	 *
	 * @return the name of this notation.
	 */
	public String getName() {
		return (name);
	}

	/**
	 * Get the external id for this notation.
	 *
	 * @return the external id for this notation.
	 */
	public External getExternalId() {
		return (externalId);
	}

////////////////////////////////////////////////////////////////////
	public static abstract class External {

		private final String system;

		/**
		 * Construct a system identifier.
		 */
		public External(String system) {
			this.system = system;
		}

		/**
		 * Get the system id for this system identifer
		 *
		 * @return the system identifier
		 */
		public String getSystem() {
			return (system);
		}
	}

	/**
	 * The system identifier.
	 */
	public static class System extends External {

		/**
		 * Construct a system identifier.
		 *
		 * @param systemId is the system identifier.
		 */
		public System(String systemId) {
			super(systemId);
		}

		/**
		 * Get the system identifier.
		 *
		 * @return th esystem identifier.
		 */
		@Override
		public String getSystem() {
			return (super.getSystem());
		}
	}

	/**
	 * The public identifier.
	 */
	public static class Public extends System {

		private final String publicId;

		public Public(String publicId, String systemId) {
			super(systemId);
			this.publicId = publicId;
		}

		/**
		 * Get the public identifier.
		 *
		 * @return the public identifier.
		 */
		public String getPublic() {
			return (publicId);
		}
	}
}
