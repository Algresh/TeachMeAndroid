package ru.tulupov.alex.teachme.presenters;

import android.graphics.Bitmap;

import java.util.List;
import java.util.Map;

import ru.tulupov.alex.teachme.models.ContactsBlock;
import ru.tulupov.alex.teachme.models.Description;
import ru.tulupov.alex.teachme.models.FullNameBlock;
import ru.tulupov.alex.teachme.models.ModelUserInfo;
import ru.tulupov.alex.teachme.models.PriceList;
import ru.tulupov.alex.teachme.views.activivties.ChangeTeacherView;
import ru.tulupov.alex.teachme.views.activivties.LoginView;


public class ChangeTeacherPresenter {

    ChangeTeacherView view;

    private ModelUserInfo model;

    public void onCreate(ChangeTeacherView view) {
        this.view = view;
        model = new ModelUserInfo();
    }


    public void checkEmailAndLogin(String email, String phone) {
        model.checkEmailAndLogin(email, phone, new ModelUserInfo.CheckLoginEmailCallBack() {
            @Override
            public void success(Map fields) {
                Double email = (Double) fields.get("email");
                Double phone = (Double) fields.get("phoneNumber");

                int err = 0;
                if (email == 1) err = err + 10;
                if (phone == 1) err = err + 100;

                if (view != null) {
                    // передается число в которое означает какие ошибки произошли
                    // Пример: 101 - ошибка в логине и в номере телефона
                    view.emailAndLoginIsChecked(err);
                }


            }

            @Override
            public void error() {
                view.emailAndLoginIsCheckedError();
            }
        });
    }


    public void showTeacherFullName(String accessToken) {
        model.getTeacherFullName(accessToken, new ModelUserInfo.ChangeTeacherFullNameCallBack() {
            @Override
            public void success(FullNameBlock block) {
                if (view != null) {
                    view.showDataFullName(block);
                }
            }

            @Override
            public void error() {
                if (view != null) {
                    view.errorDataFullName();
                }
            }
        });
    }

    public void showTeacherDescription(String accessToken) {
        model.getTeacherDescription(accessToken, new ModelUserInfo.ChangeTeacherDescriptionCallBack() {
            @Override
            public void success(Description description) {
                if (view != null) {
                    String text = description.toString();

                    view.showDataDescription(text);
                }
            }

            @Override
            public void error() {
                if (view != null) {
                    view.errorDataDescription();
                }
            }
        });
    }

    public void showTeacherSubjects(String accessToken) {
        model.getTeacherSubjects(accessToken, new ModelUserInfo.ChangeTeacherPriceListCallBack() {
            @Override
            public void success(List<PriceList> lists) {
                if (view != null) {
                    view.showDataSubjects(lists);
                }
            }

            @Override
            public void error() {
                if (view != null) {
                    view.errorDataSubjects();
                }
            }
        });
    }

    public void showTeacherContacts(String accessToken) {
        model.getTeacherContacts(accessToken, new ModelUserInfo.ChangeTeacherContactsBlockCallBack() {
            @Override
            public void success(ContactsBlock block) {
                if (view != null) {
                    view.showDataContacts(block);
                }
            }

            @Override
            public void error() {
                if (view != null) {
                    view.errorDataContacts();
                }
            }
        });
    }

    public void changeTeacherFullName(String accessToken, Map<String, String> map) {
        model.changeTeacherFullName(accessToken, map, new ModelUserInfo.ChangeTeacherFullNameCallBack() {
            @Override
            public void success(FullNameBlock block) {
                if (view != null) {
                    view.changeFullNameSuccess(block);
                }
            }

            @Override
            public void error() {
                if (view != null) {
                    view.changeFullNameError();
                }
            }
        });
    }

    public void changeTeacherFullName(final String accessToken, final Bitmap photo, final int userId, Map<String, String> map) {
        model.changeTeacherFullName(accessToken, map, new ModelUserInfo.ChangeTeacherFullNameCallBack() {
            @Override
            public void success(final FullNameBlock block) {
                model.setPhoto(accessToken, String.valueOf(userId), photo, new ModelUserInfo.ChangeTeacherPhotoCallBack() {
                    @Override
                    public void success(String photoSrc) {
                        if (view != null) {
                            block.setPhoto(photoSrc);
                            view.changeFullNameSuccess(block);
                        }
                    }

                    @Override
                    public void error(int type) {
                        if (view != null) {
                            view.changeFullNameError();
                        }
                    }
                });
            }

            @Override
            public void error() {
                if (view != null) {
                    view.changeFullNameError();
                }
            }
        });
    }

    public void changeTeacherDescription(String accessToken, String description) {
        model.changeTeacherDescription(accessToken, description, new ModelUserInfo.ChangeTeacherCallBack() {
            @Override
            public void success() {
                if (view != null) {
                    view.changeDescriptionSuccess();
                }
            }

            @Override
            public void error() {
                if (view != null) {
                    view.changeDescriptionError();
                }
            }
        });
    }

    public void changeTeacherSubjects(String accessToken, List<PriceList> lists) {
        model.changeTeacherSubjects(accessToken, lists, new ModelUserInfo.ChangeTeacherCallBack() {
            @Override
            public void success() {
                if (view != null) {
                    view.changeSubjectsSuccess();
                }
            }

            @Override
            public void error() {
                if (view != null) {
                    view.changeSubjectsError();
                }
            }
        });
    }

    public void changeTeacherContacts(String accessToken, Map<String, String> map) {
        model.changeTeacherContacts(accessToken, map, new ModelUserInfo.ChangeTeacherContactsBlockCallBack() {
            @Override
            public void success(ContactsBlock block) {
                if (view != null) {
                    view.changeContactsSuccess(block);
                }
            }

            @Override
            public void error() {
                if (view != null) {
                    view.changeContactsError();
                }
            }
        });
    }
}
