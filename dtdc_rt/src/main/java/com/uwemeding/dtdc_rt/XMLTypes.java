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


public interface XMLTypes
{
    public static final int THE_EOF  = 0;    /* End of file indicator */
    public static final int STAG     = 1;    /* Start tag */
    public static final int ETAG     = 2;    /* End tag */
    public static final int STRING   = 3;    /* Text of some kind */
    public static final int SPECIAL  = 4;    /* Special type, like <!ENTITY ...> */
    public static final int XML      = 5;    /* XML type */
}
