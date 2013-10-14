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
package com.uwemeding.dtdc_rt.tree;

import com.uwemeding.dtdc_rt.Tag;
import com.uwemeding.dtdc_rt.TagPeer;
import com.uwemeding.dtdc_rt.XMLStack;
import com.uwemeding.dtdc_rt.XMLTagHandler;
import com.uwemeding.dtdc_rt.parser.XMLCParser;
import com.uwemeding.dtdc_rt.parser.XMLLex;


/**
 * Implements the JDB.DTD parser
 */
public class XMLTreeParser extends XMLCParser
{
    public XMLTreeParser()
    {
    }

	@Override
    public Object parseXML(XMLTagHandler th, XMLLex lex) throws Exception
    {
        Tag        tag;
        XMLTree    tree = new XMLTree();
        XMLStack   stack = new XMLStack();

        while((tag = lex.getNextTag()) != null)
        {
            switch(tag.type)
            {
            case Tag.SPECIAL:
                if(tag.name.startsWith("!--"))
                    ; // ignore comments
                else
                    tree.addTag(tag);
                break;
            case Tag.STAG:
                if(tag.hasImplicitEndTag)
                    tag.ref = tag;
                else
                    stack.push(tag);
                tree.addTag(tag);
                break;
            case Tag.ETAG:
                Tag startTag = (Tag)stack.pop();
                startTag.ref = tag;
                tree.addTag(tag);
                break;
            case Tag.STRING:
                tree.addTag(tag);
                break;
            }
        }
        return(tree);

    }
} 
