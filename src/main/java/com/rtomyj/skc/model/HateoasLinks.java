package com.rtomyj.skc.model;

import java.util.List;

public interface HateoasLinks
{

    void setSelfLink();
    void setLinks();

    static <T extends HateoasLinks> void setLinks(List<T> instances)
    {
        instances.forEach(T::setLinks);
    }

}
