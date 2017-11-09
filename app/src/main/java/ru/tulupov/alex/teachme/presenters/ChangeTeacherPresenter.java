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
    private boolean isLoading = false;

    public void onCreate(ChangeTeacherView view) {
        this.view = view;
        model = new ModelUserInfo();
    }


    public void checkEmailAndLogin(String email, String phone) {
        if (isLoading) return;
        isLoading = true;

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
                    isLoading = false;
                    view.emailAndLoginIsChecked(err);
                }


            }

            @Override
            public void error() {
                isLoading = false;
                if (view != null) view.emailAndLoginIsCheckedError();
            }
        });
    }


    public void showTeacherFullName(String accessToken) {
        if (isLoading) return;
        isLoading = true;

        model.getTeacherFullName(accessToken, new ModelUserInfo.ChangeTeacherFullNameCallBack() {
            @Override
            public void success(FullNameBlock block) {
                if (view != null) {
                    isLoading = false;
                    view.showDataFullName(block);
                }
            }

            @Override
            public void error() {
                if (view != null) {
                    isLoading = false;
                    view.errorDataFullName();
                }
            }
        });
    }

    public void showTeacherDescription(String accessToken) {
        if (isLoading) return;
        isLoading = true;

        model.getTeacherDescription(accessToken, new ModelUserInfo.ChangeTeacherDescriptionCallBack() {
            @Override
            public void success(Description description) {
                if (view != null) {
                    String text = description.toString();
                    isLoading = false;
                    view.showDataDescription(text);
                }
            }

            @Override
            public void error() {
                if (view != null) {
                    isLoading = false;
                    view.errorDataDescription();
                }
            }
        });
    }

    public void showTeacherSubjects(String accessToken) {
        if (isLoading) return;
        isLoading = true;

        model.getTeacherSubjects(accessToken, new ModelUserInfo.ChangeTeacherPriceListCallBack() {
            @Override
            public void success(List<PriceList> lists) {
                if (view != null) {
                    isLoading = false;
                    view.showDataSubjects(lists);
                }
            }

            @Override
            public void error() {
                if (view != null) {
                    isLoading = false;
                    view.errorDataSubjects();
                }
            }
        });
    }

    public void showTeacherContacts(String accessToken) {
        if (isLoading) return;
        isLoading = true;

        model.getTeacherContacts(accessToken, new ModelUserInfo.ChangeTeacherContactsBlockCallBack() {
            @Override
            public void success(ContactsBlock block) {
                if (view != null) {
                    isLoading = false;
                    view.showDataContacts(block);
                }
            }

            @Override
            public void error() {
                if (view != null) {
                    isLoading = false;
                    view.errorDataContacts();
                }
            }
        });
    }

    public void changeTeacherFullName(String accessToken, Map<String, String> map) {
        if (isLoading) return;
        isLoading = true;

        model.changeTeacherFullName(accessToken, map, new ModelUserInfo.ChangeTeacherFullNameCallBack() {
            @Override
            public void success(FullNameBlock block) {
                if (view != null) {
                    isLoading = false;
                    view.changeFullNameSuccess(block);
                }
            }

            @Override
            public void error() {
                if (view != null) {
                    isLoading = false;
                    view.changeFullNameError();
                }
            }
        });
    }

    public void changeTeacherFullName(final String accessToken, final Bitmap photo, final int userId, Map<String, String> map) {
        if (isLoading) return;
        isLoading = true;

        model.changeTeacherFullName(accessToken, map, new ModelUserInfo.ChangeTeacherFullNameCallBack() {
            @Override
            public void success(final FullNameBlock block) {
                model.setPhoto(accessToken, String.valueOf(userId), photo, new ModelUserInfo.ChangeTeacherPhotoCallBack() {
                    @Override
                    public void success(String photoSrc) {
                        if (view != null) {
                            block.setPhoto(photoSrc);
                            isLoading = false;
                            view.changeFullNameSuccess(block);

                        }
                    }

                    @Override
                    public void error(int type) {
                        if (view != null) {
                            isLoading = false;
                            view.changeFullNameError();
                        }

                    }
                });
            }

            @Override
            public void error() {
                if (view != null) {
                    isLoading = false;
                    view.changeFullNameError();
                }

            }
        });
    }

    public void changeTeacherDescription(String accessToken, String description) {
        if (isLoading) return;
        isLoading = true;

        model.changeTeacherDescription(accessToken, description, new ModelUserInfo.ChangeTeacherCallBack() {
            @Override
            public void success() {
                if (view != null) {
                    isLoading = false;
                    view.changeDescriptionSuccess();
                }
            }

            @Override
            public void error() {
                if (view != null) {
                    isLoading = false;
                    view.changeDescriptionError();
                }
            }
        });
    }

    public void changeTeacherSubjects(String accessToken, List<PriceList> lists) {
        if (isLoading) return;
        isLoading = true;

        model.changeTeacherSubjects(accessToken, lists, new ModelUserInfo.ChangeTeacherCallBack() {
            @Override
            public void success() {
                if (view != null) {
                    isLoading = false;
                    view.changeSubjectsSuccess();
                }
            }

            @Override
            public void error() {
                if (view != null) {
                    isLoading = false;
                    view.changeSubjectsError();
                }
            }
        });
    }

    public void changeTeacherContacts(String accessToken, Map<String, String> map) {
        if (isLoading) return;
        isLoading = true;

        model.changeTeacherContacts(accessToken, map, new ModelUserInfo.ChangeTeacherContactsBlockCallBack() {
            @Override
            public void success(ContactsBlock block) {
                if (view != null) {
                    isLoading = false;
                    view.changeContactsSuccess(block);
                }

            }

            @Override
            public void error() {
                if (view != null) {
                    isLoading = false;
                    view.changeContactsError();
                }
            }
        });
    }
}
